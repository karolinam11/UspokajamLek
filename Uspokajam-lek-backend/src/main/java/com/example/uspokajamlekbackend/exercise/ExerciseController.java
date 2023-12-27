package com.example.uspokajamlekbackend.exercise;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("/add")
    public ResponseEntity<?> addExercise(@RequestBody ExerciseRequest exerciseRequest) {
        exerciseService.addExercise(exerciseRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody ExerciseRequest exerciseRequest) {
        exerciseService.updateExercise(exerciseRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteExercise(@RequestParam String name) {
        exerciseService.deleteExercise(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

}
