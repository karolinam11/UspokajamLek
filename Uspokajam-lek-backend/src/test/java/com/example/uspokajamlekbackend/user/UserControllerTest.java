package com.example.uspokajamlekbackend.user;

import com.example.uspokajamlekbackend.exercise.ExerciseService;
import com.example.uspokajamlekbackend.user.dto.UserResponse;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void shouldLogin() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("email")
                .password("password")
                .build();
        UserResponse userResponse = UserResponse.builder()
                .user(new Patient())
                .token("token")
                .build();
        when(userService.login("email", "password")).thenReturn(userResponse);
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1)).login("email", "password");
    }

    @Test
    public void shouldNotLoginWhenCredentialsIncorrect() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("email")
                .password("password")
                .build();
        when(userService.login("email", "password")).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
