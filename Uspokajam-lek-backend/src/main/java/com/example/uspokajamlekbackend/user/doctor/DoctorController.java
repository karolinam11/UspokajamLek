package com.example.uspokajamlekbackend.user.doctor;

import com.example.uspokajamlekbackend.user.doctor.dto.AddPatientRequest;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.user.doctor.dto.EditDoctorAccountRequest;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorSignupRequest;
import com.example.uspokajamlekbackend.user.patient.Role;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(this.doctorService.getDoctorById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup-doctor")
    public ResponseEntity<?> signup(@RequestBody DoctorSignupRequest signupRequest) {
        Doctor doctor = modelMapper.map(signupRequest, Doctor.class);
        try {
            Doctor doctorDb = doctorService.signup(doctor);
            return ResponseEntity.ok(modelMapper.map(doctorDb, DoctorResponse.class));
        } catch (EntityExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
    }

    @PutMapping("/edit-doctor-account")
    public ResponseEntity<?> editDoctorAccount(@RequestBody EditDoctorAccountRequest editAccountRequest) {
        try {
            Doctor doctor = modelMapper.map(editAccountRequest, Doctor.class);
            Doctor updatedDoctor = doctorService.editDoctorAccount(doctor);
            return ResponseEntity.ok(modelMapper.map(updatedDoctor, DoctorResponse.class));
        } catch (EntityExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add-patient")
    public ResponseEntity<?> addPatient(@RequestBody AddPatientRequest addPatientRequest) {
        if(this.doctorService.assignPatientToDoctor(addPatientRequest)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/patients")
    public ResponseEntity<?> getPatients(@RequestParam Long id) {
        return ResponseEntity.ok(this.doctorService.getPatients(id));
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<?> getPendingRequests(@RequestParam long id) {
        return ResponseEntity.ok(doctorService.getPendingRequests(id));
    }

    @PostMapping("/accept-pending-request")
    public ResponseEntity<?> acceptPendingRequest(@RequestBody AddPatientRequest addPatientRequest) {
        doctorService.acceptDoctorRequest(addPatientRequest.getPatientId(), addPatientRequest.getDoctorId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline-pending-request")
    public ResponseEntity<?> declinePendingRequest(@RequestBody AddPatientRequest addPatientRequest) {
       try{
           doctorService.declineDoctorRequest(addPatientRequest.getPatientId(), addPatientRequest.getDoctorId());
           return ResponseEntity.ok().build();
       } catch (EntityNotFoundException e){
           return ResponseEntity.notFound().build();
       }
    }

}
