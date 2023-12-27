package com.example.uspokajamlekbackend.exercise;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean deleteByName(String name); //Usuwa ćwiczenie o danej nazwie

    Exercise getByName(String name);
}
