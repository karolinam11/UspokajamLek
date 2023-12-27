package com.example.uspokajamlekbackend.user.doctor;

import com.example.uspokajamlekbackend.user.doctor.dto.AddPatientRequest;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import com.example.uspokajamlekbackend.user.patient.Role;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Doctor getById(Long doctorId) {
        return doctorRepository.getById(doctorId);
    }

    public Doctor signup(Doctor doctor) {
        doctor.setRole(Role.DOCTOR);
        if (!doctorRepository.existsByEmail(doctor.getEmail())) {
            doctor.setInvitationCode(generateInvitationCode());
            return doctorRepository.save(doctor);
        } else {
            throw new EntityExistsException();
        }
    }

    public Doctor editDoctorAccount(Doctor doctor) {
        Doctor doctorDb = doctorRepository.findByEmail(doctor.getEmail()).orElseThrow(EntityNotFoundException::new);
        doctor.setId(doctorDb.getId());
        return doctorRepository.save(doctor);
    }

    public boolean assignPatientToDoctor(AddPatientRequest addPatientRequest) {
        Doctor doctor = doctorRepository.findById(addPatientRequest.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + addPatientRequest.getDoctorId()));
        Patient patient = patientRepository.findById(addPatientRequest.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + addPatientRequest.getPatientId()));
        if (!doctor.getPatients().contains(patient)) {
            patient.getDoctors().add(doctor);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }


    public List<PatientResponse> getPatients(Long doctorId) {
        Doctor doctorDb = doctorRepository.findById(doctorId).orElseThrow(EntityNotFoundException::new);
        return doctorDb.getPatients().stream().map(patient -> modelMapper.map(patient, PatientResponse.class)).toList();
    }

    public void acceptDoctorRequest(long patientId, long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));
        if (!doctor.getPatients().contains(patient) && doctor.getPendingRequests().contains(patient)) {
            patient.getDoctors().add(doctor);
            doctor.getPendingRequests().remove(patient);
            patientRepository.save(patient);
            doctor.getPatients().add(patient);
            doctor.getPendingRequests().remove(patient);
            doctorRepository.save(doctor);
        }
    }

    public void declineDoctorRequest(long patientId, long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));
        if (!doctor.getPatients().contains(patient) && doctor.getPendingRequests().contains(patient)) {
            doctor.getPendingRequests().remove(patient);
            doctorRepository.save(doctor);
        }
    }

    public List<PatientResponse> getPendingRequests(long doctorId) {
        Doctor doctorDb = doctorRepository.findById(doctorId).orElseThrow(EntityNotFoundException::new);
        return doctorDb.getPendingRequests().stream().map(patient -> modelMapper.map(patient, PatientResponse.class)).toList();

    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId).orElseThrow(EntityNotFoundException::new);
    }

    private String generateInvitationCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);
    }

}
