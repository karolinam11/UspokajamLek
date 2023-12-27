package com.example.uspokajamlekbackend.dailyReport;

import com.example.uspokajamlekbackend.user.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity //encja, czyli bedzie zapis do bazy danych
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data //tworzy nam gettery i settery
@ToString
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String mood;
    private String note;

    @ManyToOne
    @ToString.Exclude
    private Patient patientDailyReport;

}
