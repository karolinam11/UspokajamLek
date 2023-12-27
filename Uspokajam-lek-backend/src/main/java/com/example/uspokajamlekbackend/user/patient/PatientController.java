package com.example.uspokajamlekbackend.user.patient;

import com.example.uspokajamlekbackend.user.patient.dto.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            Patient registeredPatient = patientService.signup(modelMapper.map(signupRequest, Patient.class));
            return ResponseEntity.ok(modelMapper.map(registeredPatient, PatientResponse.class));
        } catch (EntityExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
    }


    @PutMapping("edit-account")
    public ResponseEntity<?> editAccount(@RequestBody EditAccountRequest editAccountRequest) {
        try {
            Patient editedPatient = patientService.editAccount(editAccountRequest);
            return ResponseEntity.ok(modelMapper.map(editedPatient, PatientResponse.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("add-doctor-request")
    public ResponseEntity<?> addDoctor(@RequestBody AddDoctorRequest addDoctorRequest) {
        try {
            if (patientService.createDoctorRequest(addDoctorRequest)) {
                return ResponseEntity.ok().build();
            }
        } catch (EntityNotFoundException ignored) {
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("my-doctors/{id}")
    public ResponseEntity<?> getMyDoctors(@PathVariable Long id) {
        return ResponseEntity.ok(this.patientService.getMyDoctors(id));
    }

}
