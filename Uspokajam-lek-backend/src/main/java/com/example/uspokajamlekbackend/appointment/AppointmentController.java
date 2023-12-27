package com.example.uspokajamlekbackend.appointment;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("doctor-appointments")
    public ResponseEntity<?> getDoctorAppointments(@RequestParam Long id){
        return ResponseEntity.ok(appointmentService.getFutureDoctorAppointment(id));
    }

    @GetMapping("patient-appointments")
    public ResponseEntity<?> getPatientAppointments(@RequestParam Long id){
        return ResponseEntity.ok(appointmentService.getFuturePatientAppointment(id));
    }

    @PostMapping("add-appointment")
    public ResponseEntity<?> addAppointment(@RequestBody AddAppointmentRequest addAppointmentRequest){
        if(addAppointmentRequest.getVisitStartDate().isAfter(addAppointmentRequest.getVisitEndDate()) ||
            addAppointmentRequest.getVisitStartDate().isBefore(LocalDateTime.now()) || addAppointmentRequest.getVisitEndDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(appointmentService.addAppointment(addAppointmentRequest));
    }

    @GetMapping("patient-past-appointments")
    public ResponseEntity<?> getPastPatientAppointments(@RequestParam long id){
        return ResponseEntity.ok(appointmentService.getPastPatientAppointments(id));
    }

    @GetMapping("doctor-past-appointments")
    public ResponseEntity<?> getPastDoctorAppointments(@RequestParam long id){
        return ResponseEntity.ok(appointmentService.getPastDoctorAppointments(id));
    }

    @DeleteMapping("remove-appointment")
    public ResponseEntity<?> removeAppointment(@RequestParam long id){
        if (appointmentService.removeAppointment(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
