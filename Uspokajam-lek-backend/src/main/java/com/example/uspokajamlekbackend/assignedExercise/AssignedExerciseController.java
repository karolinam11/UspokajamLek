package com.example.uspokajamlekbackend.assignedExercise;

import com.example.uspokajamlekbackend.assignedExercise.dto.AssignExerciseRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
public class AssignedExerciseController {

    @Autowired
    private AssignedExerciseService assignedExerciseService;

    @PostMapping("assign-exercise")
    public ResponseEntity<?> assignExercise(@RequestBody AssignExerciseRequest assignExerciseRequest) {
        if (assignExerciseRequest.getDueDate().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }
        assignedExerciseService.assignExercise(assignExerciseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("patient-assigned-exercises")
    public ResponseEntity<?> getAssignedExercises(@RequestParam long id){
        return ResponseEntity.ok(assignedExerciseService.getUserAssignedExercises(id));
    }

    @GetMapping("patient-new-assigned-exercises")
    public ResponseEntity<?> getNewAssignedExercises(@RequestParam long id){
        return ResponseEntity.ok(assignedExerciseService.getNewUserAssignedExercises(id));
    }

    @GetMapping("exercises-done-last-week")
    public ResponseEntity<?> getExercisesDoneLastWeek(@RequestParam long id){
        return ResponseEntity.ok(assignedExerciseService.getUserAssignedExercisesLastWeek(id));
    }

    @GetMapping("patient-assigned-exercises-today")
    public ResponseEntity<?> getAssignedExercisesToday(@RequestParam long id){
        return ResponseEntity.ok(assignedExerciseService.getUserAssignedExercisesToday(id));
    }

    @DeleteMapping("remove-assigned-exercise")
    public ResponseEntity<?> removeAssignedExercise(@RequestParam long id) {
        if (assignedExerciseService.removeAssignedExercise(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("set-exercise-status")
    public ResponseEntity<?> setExerciseStatus(@RequestParam long id) {
        if (assignedExerciseService.setExerciseStatus(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
