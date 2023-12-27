package com.example.uspokajamlekbackend.dailyReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

    int countAllByDateAndPatientDailyReportEmail(LocalDate date, String email);

    List<DailyReport> getAllByPatientDailyReportIdOrderByDate(Long id);
    List<DailyReport> getAllByPatientDailyReportId(Long userId);


    @Query("FROM DailyReport d WHERE d.date > :date AND d.patientDailyReport.id= :userId")
    List<DailyReport> getAllByPatientAndDate(Long userId, LocalDate date);

}
