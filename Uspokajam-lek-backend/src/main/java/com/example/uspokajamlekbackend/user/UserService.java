package com.example.uspokajamlekbackend.user;

import com.example.uspokajamlekbackend.security.JwtTokenUtil;
import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorRepository;
import com.example.uspokajamlekbackend.user.dto.UserResponse;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientRepository;
import com.example.uspokajamlekbackend.user.patient.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserResponse login(String email, String password) {
        Optional<Patient> patient = patientRepository.findByEmailAndPassword(email,password);
        if(patient.isPresent()){
            Patient loggedInPatient = patient.get();
            return new UserResponse(loggedInPatient,jwtTokenUtil.generateToken(loggedInPatient.getEmail(), Role.PATIENT));
        }
        Optional<Doctor> doctor = doctorRepository.findByEmailAndPassword(email,password);
        if(doctor.isPresent()){
            Doctor loggedInDoctor = doctor.get();
            return new UserResponse(loggedInDoctor,jwtTokenUtil.generateToken(loggedInDoctor.getEmail(), Role.DOCTOR));
        }
        throw new EntityNotFoundException();
    }
}