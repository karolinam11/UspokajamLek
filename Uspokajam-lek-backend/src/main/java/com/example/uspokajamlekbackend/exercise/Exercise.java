package com.example.uspokajamlekbackend.exercise;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Entity //encja, czyli bedzie zapis do bazy danych
@AllArgsConstructor
@RequiredArgsConstructor
@Data //tworzy nam gettery i settery
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(columnDefinition = "VARCHAR(1000)")
    private String description;

    private String duration;
    private int time;
    private String category;

    @ManyToOne
    private Doctor createdBy;

    @Override
    public String toString() {
        return "Exercise [id=" + id + ", name=" + name + ", description=" + description + "]";
    }
}
