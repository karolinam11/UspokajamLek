package com.example.uspokajamlekbackend.activity;

import com.example.uspokajamlekbackend.user.patient.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private PatientService patientService;

    @Autowired
    private ModelMapper modelMapper;

    public List<ActivityResponse> getUserActivities(Long userId) {
        return activityRepository.getActivitiesByPatientActivityId(userId).stream().map(
                activity -> modelMapper.map(activity, ActivityResponse.class)
        ).toList();
    }

    public void addActivity(ActivityRequest activityRequest) {
        Activity activity = modelMapper.map(activityRequest, Activity.class);
        activity.setPatientActivity(patientService.getById(activityRequest.getUserId()));
        activityRepository.save(activity);
    }
}
