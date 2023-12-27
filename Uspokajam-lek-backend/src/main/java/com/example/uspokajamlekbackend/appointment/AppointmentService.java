package com.example.uspokajamlekbackend.appointment;

import com.example.uspokajamlekbackend.user.doctor.DoctorService;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientService;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ModelMapper modelMapper;

    public AppointmentResponse addAppointment(AddAppointmentRequest addAppointmentRequest) {
        Appointment appointment = modelMapper.map(addAppointmentRequest, Appointment.class);
        appointment.setDoctor(doctorService.getById(addAppointmentRequest.getDoctorId()));
        appointment.setPatient(patientService.getById(addAppointmentRequest.getPatientId()));
        appointment = this.appointmentRepository.save(appointment);
        return createAppointmentResponse(this.appointmentRepository.save(appointment));

    }

    public List<AppointmentResponse> getFutureDoctorAppointment(Long doctorId) {
        return this.appointmentRepository.getAppointmentByDoctorId(doctorId).stream().filter(
                appointment -> appointment.getVisitStartDate().isAfter(LocalDateTime.now())
        ).map(appointment -> createAppointmentResponse(appointment)).toList();
    }

    public List<AppointmentResponse> getFuturePatientAppointment(Long patientId) {
        return this.appointmentRepository.getAppointmentByPatientId(patientId).stream().filter(
                appointment -> appointment.getVisitStartDate().isAfter(LocalDateTime.now())
        ).map(appointment -> createAppointmentResponse(appointment)).toList();
    }

    public List<AppointmentResponse> getPastPatientAppointments(Long patientId) {
        return this.appointmentRepository.getAppointmentByPatientId(patientId).stream().filter(
                appointment -> appointment.getVisitStartDate().isBefore(LocalDateTime.now())
        ).map(appointment -> createAppointmentResponse(appointment)).toList();
    }

    public List<AppointmentResponse> getPastDoctorAppointments(Long doctorId) {
        return this.appointmentRepository.getAppointmentByDoctorId(doctorId).stream().filter(
                appointment -> appointment.getVisitStartDate().isBefore(LocalDateTime.now())
        ).map(appointment -> createAppointmentResponse(appointment)).toList();
    }


    public AppointmentResponse createAppointmentResponse(Appointment appointment) {
        AppointmentResponse appointmentResponse = modelMapper.map(appointment, AppointmentResponse.class);
        appointmentResponse.setDoctor(modelMapper.map(appointment.getDoctor(), DoctorResponse.class));
        appointmentResponse.setPatient(modelMapper.map(appointment.getPatient(), PatientResponse.class));
        return appointmentResponse;
    }

    public boolean removeAppointment(Long appointmentId){
        Appointment appointment = appointmentRepository.getById(appointmentId);
        if (appointment != null){
            appointment.getDoctor().getAppointments().remove(appointment);
            appointmentRepository.deleteById(appointment.getId());
            return true;
        }
        return false;
    }

}
