package com.example.uspokajamlekbackend.dailyReport;

import com.example.uspokajamlekbackend.dailyReport.dto.DailyReportRequest;
import com.example.uspokajamlekbackend.user.patient.PatientService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class DailyReportService {

    @Autowired
    private DailyReportRepository dailyReportRepository;
    @Autowired
    private PatientService patientService;

    @Autowired
    private ModelMapper modelMapper;

    public List<DailyReport> getUserDailyReports(Long userId) {
        return dailyReportRepository.getAllByPatientDailyReportId(userId);
    }

    public void addDailyReport(DailyReportRequest dailyReportRequest, String email) {
        DailyReport dailyReport = modelMapper.map(dailyReportRequest, DailyReport.class);
        dailyReport.setPatientDailyReport(patientService.getByEmail(email));
        dailyReportRepository.save(dailyReport);
    }

    public boolean checkIfDailyReportCanBeAdded(LocalDate localDate, String email) {
        return dailyReportRepository.countAllByDateAndPatientDailyReportEmail(localDate, email) < 1;
    }

    public List<Mood> getMoods(int numOfDays, Long userId) {
        List<DailyReport> reports = dailyReportRepository.getAllByPatientDailyReportId(userId);
        LocalDate dateDaysBefore = LocalDate.now().minusDays(numOfDays);
        List<Mood> moods = reports.stream().map((report -> new Mood(report.getMood(), report.getDate()))).toList();
        return moods.stream().filter((mood -> mood.getDate().isAfter(dateDaysBefore))).toList();
    }

    public List<Integer> getMoodsQuantityInLastXDays(Long userId, int lastXDays) {
        LocalDate date = LocalDate.now().minusDays(lastXDays);
        List<DailyReport> reports = dailyReportRepository.getAllByPatientAndDate(userId, date);
        int terrible = (int) reports.stream().filter(report -> report.getMood().equals("terrible")).count();
        int bad = (int) reports.stream().filter(report -> report.getMood().equals("bad")).count();
        int neutral = (int) reports.stream().filter(report -> report.getMood().equals("neutral")).count();
        int good = (int) reports.stream().filter(report -> report.getMood().equals("good")).count();
        int excellent = (int) reports.stream().filter(report -> report.getMood().equals("excellent")).count();
        return List.of(terrible, bad, neutral, good, excellent);
    }

    public DailyReport getById(long id) {
        return this.dailyReportRepository.getById(id);
    }

    public int getLongestStreak(Long userId) {
        List<DailyReport> reports = this.dailyReportRepository.getAllByPatientDailyReportIdOrderByDate(userId);
        return findLongestStreak(reports);
    }

    public int getCurrentStreak(Long userId) {
        List<LocalDate> dates = this.dailyReportRepository.getAllByPatientDailyReportIdOrderByDate(userId)
                .stream()
                .map(report -> report.getDate())
                .toList();
        return findCurrentStreak(dates);
    }

    public void removeReport(long id) {
        this.dailyReportRepository.delete(this.getById(id));
    }

    private int findLongestStreak(List<DailyReport> dates) {
        if (dates.isEmpty()) {
            return 0;
        }

        int maxStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < dates.size(); i++) {
            LocalDate currentDate = dates.get(i).getDate();
            LocalDate previousDate = dates.get(i - 1).getDate();

            if (currentDate.minusDays(1).equals(previousDate)) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }

            maxStreak = Math.max(maxStreak, currentStreak);
        }

        return maxStreak;
    }

    private int findCurrentStreak(List<LocalDate> dates) {
        if (dates.isEmpty()) {
            return 0;
        }

        int currentStreak = 0;
        int maxStreak = 0;

        LocalDate today = LocalDate.now();

        for (LocalDate date : dates) {
            if (date.isEqual(today) || date.isEqual(today.minusDays(1))) {
                currentStreak++;
            } else {
                maxStreak = Math.max(maxStreak, currentStreak);
                currentStreak = 0;
            }
        }

        return Math.max(maxStreak, currentStreak);
    }
}