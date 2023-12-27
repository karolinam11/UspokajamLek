package com.example.uspokajamlekbackend.dailyReport;

import com.example.uspokajamlekbackend.dailyReport.dto.DailyReportRequest;
import com.example.uspokajamlekbackend.dailyReport.dto.MoodsRequest;
import com.example.uspokajamlekbackend.user.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/daily-reports")
public class DailyReportController {

    @Autowired
    private DailyReportService dailyReportService;

    @GetMapping
    public ResponseEntity<List<DailyReport>> getUserDailyReports(@RequestParam Long id) {
        return ResponseEntity.ok(dailyReportService.getUserDailyReports(id));
    }

    @GetMapping("/today")
    public ResponseEntity<Boolean> checkIfDailyReportAddedToday(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(dailyReportService.checkIfDailyReportCanBeAdded(LocalDate.now(), email));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDailyReport(@RequestBody DailyReportRequest dailyReportRequest, @AuthenticationPrincipal String username) {
        if (this.dailyReportService.checkIfDailyReportCanBeAdded(dailyReportRequest.getDate(), username)) {
            this.dailyReportService.addDailyReport(dailyReportRequest, username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/moods")
    public ResponseEntity<?> getMoods(@RequestBody MoodsRequest moodsRequest) {
        return ResponseEntity.ok(this.dailyReportService.getMoods(moodsRequest.getNumOfDays(), moodsRequest.getUserId()));
    }

    @GetMapping("/moods-quantity")
    public ResponseEntity<?> getMoodsQuantity(Long userId, int days) {
        return ResponseEntity.ok(this.dailyReportService.getMoodsQuantityInLastXDays(userId,days));
    }

    @GetMapping("/longest-streak")
    public ResponseEntity<?> getLongestStreak(@RequestParam Long userId) {
        return ResponseEntity.ok(this.dailyReportService.getLongestStreak(userId));
    }

    @GetMapping("/current-streak")
    public ResponseEntity<?> getCurrentStreak(@RequestParam Long userId) {
        return ResponseEntity.ok(this.dailyReportService.getCurrentStreak(userId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReport(@RequestParam Long id) {
        this.dailyReportService.removeReport(id);
        return ResponseEntity.ok().build();
    }


}
