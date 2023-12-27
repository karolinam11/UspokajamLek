package com.example.uspokajamlekbackend.user.patient;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorRepository;
import com.example.uspokajamlekbackend.user.patient.dto.AddDoctorRequest;
import com.example.uspokajamlekbackend.user.patient.dto.EditAccountRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ModelMapper modelMapper;

    public Patient signup(Patient patient) {
        if(patientRepository.existsByEmail(patient.getEmail())) {
            throw new EntityExistsException();
        }
        patient.setRole(Role.PATIENT);
        patient.setDoctors(Collections.emptyList());
        return patientRepository.save(patient);
    }

    public Patient getById(Long id) {
        return patientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Patient getByEmail(String email){return patientRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);}
    public Patient editAccount(EditAccountRequest editAccountRequest) {
        Patient patient = modelMapper.map(editAccountRequest, Patient.class);
        Patient patientDb = patientRepository.findByEmail(patient.getEmail()).orElseThrow(EntityNotFoundException::new);
        patientDb.setName(patient.getName());
        patientDb.setSurname(patient.getSurname());
        patientDb.setBirthDate(patient.getBirthDate());
        patientDb.setEmail(patient.getEmail());
        return patientRepository.save(patientDb);
    }

    public List<Doctor> getMyDoctors(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));
        return patient.getDoctors();
    }

    public boolean createDoctorRequest(AddDoctorRequest addDoctorRequest) {
        Doctor doctor = doctorRepository.findByInvitationCode(addDoctorRequest.getInvitationCode())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with invitationCode: " + addDoctorRequest.getInvitationCode()));
        Patient patient = patientRepository.findById(addDoctorRequest.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + addDoctorRequest.getPatientId()));
        if (!doctor.getPatients().contains(patient) && !doctor.getPendingRequests().contains(patient)) {
            doctor.getPendingRequests().add(patient);
            doctorRepository.save(doctor);
            return true;
        } else {
            return false;
        }
    }

}
