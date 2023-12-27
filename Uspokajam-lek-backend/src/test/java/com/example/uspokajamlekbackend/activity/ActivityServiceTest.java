package com.example.uspokajamlekbackend.activity;

import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private PatientService patientService;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private ActivityService activityService;


    @Test
    void shouldGetUserActivities() {
        // given
        Long userId = 1L;
        Activity activityDb = new Activity();
        activityDb.setId(1L);
        activityDb.setName("name");
        activityDb.setMood("mood");
        activityDb.setDate(LocalDate.of(2022,10,10));
        activityDb.setPatientActivity(new Patient());

        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setName("name");
        activityResponse.setMood("mood");
        activityResponse.setDate(LocalDate.of(2022,10,10));

        //when
        when(activityRepository.getActivitiesByPatientActivityId(userId))
                .thenReturn(List.of(activityDb));


        List<ActivityResponse> result = activityService.getUserActivities(userId);

        //then
        assertEquals(1, result.size());
        assertEquals(activityResponse.getDate(), result.get(0).getDate());
        assertEquals(activityResponse.getMood(), result.get(0).getMood());

        verify(activityRepository, times(1)).getActivitiesByPatientActivityId(userId);
        verify(modelMapper, times(1)).map(activityDb, ActivityResponse.class);
    }

    @Test
    void shouldAddActivity() {
        // given
        ActivityRequest activityRequest = new ActivityRequest();
        activityRequest.setUserId(1L);

        Activity activity = new Activity();
        activity.setId(1L);

        // when
        when(patientService.getById(activityRequest.getUserId()))
                .thenReturn(new Patient());
        activityService.addActivity(activityRequest);

        //then
        verify(modelMapper, times(1)).map(activityRequest, Activity.class);
        verify(patientService, times(1)).getById(activityRequest.getUserId());
        verify(activityRepository, times(1)).save(any(Activity.class));
    }
}
