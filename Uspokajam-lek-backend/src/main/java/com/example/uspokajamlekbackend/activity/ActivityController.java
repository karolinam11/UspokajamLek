package com.example.uspokajamlekbackend.activity;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestParam Long id){
        return ResponseEntity.ok(activityService.getUserActivities(id));
    }
    @PostMapping("/add")
    public ResponseEntity<?> addActivity(@RequestBody ActivityRequest activityRequest){
        this.activityService.addActivity(activityRequest);
        return ResponseEntity.ok().build();
    }
}
