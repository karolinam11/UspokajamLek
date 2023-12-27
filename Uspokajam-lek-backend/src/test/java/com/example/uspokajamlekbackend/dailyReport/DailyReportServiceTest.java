package com.example.uspokajamlekbackend.dailyReport;

import com.example.uspokajamlekbackend.dailyReport.dto.DailyReportRequest;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DailyReportServiceTest {

    @Mock
    private DailyReportRepository dailyReportRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DailyReportService dailyReportService;

    @Test
    void shouldGetUserDailyReports() {
        Long userId = 1L;
        when(dailyReportRepository.getAllByPatientDailyReportId(userId)).thenReturn(Arrays.asList(new DailyReport(), new DailyReport()));

        List<DailyReport> userDailyReports = dailyReportService.getUserDailyReports(userId);

        assertEquals(2, userDailyReports.size());
    }

    @Test
    void shouldAddDailyReport() {
        DailyReportRequest dailyReportRequest = new DailyReportRequest();

        DailyReport dailyReport = new DailyReport();
        when(modelMapper.map(dailyReportRequest, DailyReport.class)).thenReturn(dailyReport);
        when(patientService.getByEmail(any(String.class))).thenReturn(new Patient());

        dailyReportService.addDailyReport(dailyReportRequest, "email@example.com");

        verify(dailyReportRepository, times(1)).save(dailyReport);
    }

    @Test
    void shouldCheckIfDailyReportCanBeAdded() {
        LocalDate localDate = LocalDate.now();
        String username = "username";

        when(dailyReportRepository.countAllByDateAndPatientDailyReportEmail(localDate, username)).thenReturn(0);

        boolean canBeAdded = dailyReportService.checkIfDailyReportCanBeAdded(localDate, username);

        assertTrue(canBeAdded);
    }

    @Test
    void shouldGetMoods() {
        int numOfDays = 7;
        Long userId = 1L;

        List<DailyReport> reports = Arrays.asList(
                new DailyReport(1L, LocalDate.now(), "terrible", "note", new Patient()),
                new DailyReport(2L, LocalDate.now().minusDays(3), "bad", "note", new Patient()),
                new DailyReport(3L, LocalDate.now().minusDays(5), "neutral", "note", new Patient())
        );

        when(dailyReportRepository.getAllByPatientDailyReportId(userId)).thenReturn(reports);

        List<Mood> moods = dailyReportService.getMoods(numOfDays, userId);

        assertEquals(3, moods.size());
    }

    @Test
    void shouldGetMoodsQuantity() {
        Long userId = 1L;

        List<DailyReport> reports = Arrays.asList(
                new DailyReport(1L, LocalDate.now(), "terrible", "note", new Patient()),
                new DailyReport(2L, LocalDate.now(), "bad", "note", new Patient()),
                new DailyReport(3L, LocalDate.now(), "neutral", "note", new Patient()),
                new DailyReport(4L, LocalDate.now(), "good", "note", new Patient()),
                new DailyReport(5L, LocalDate.now(), "excellent", "note", new Patient())

        );

        when(dailyReportRepository.getAllByPatientAndDate(any(Long.class), any(LocalDate.class))).thenReturn(reports);

        List<Integer> moodsQuantity = dailyReportService.getMoodsQuantityInLastXDays(userId, 7);

        assertEquals(1, moodsQuantity.get(0));
        assertEquals(1, moodsQuantity.get(1));
        assertEquals(1, moodsQuantity.get(2));
        assertEquals(1, moodsQuantity.get(3));
        assertEquals(1, moodsQuantity.get(4));
    }

    @Test
    void shouldGetById() {
        Long id = 1L;
        DailyReport report = new DailyReport();
        when(dailyReportRepository.getById(id)).thenReturn(report);

        DailyReport retrievedReport = dailyReportService.getById(id);

        assertEquals(report, retrievedReport);
    }

    @Test
    void shouldRemoveReport() {
        Long id = 1L;
        DailyReport report = new DailyReport();
        when(dailyReportRepository.getById(id)).thenReturn(report);

        dailyReportService.removeReport(id);

        verify(dailyReportRepository, times(1)).delete(report);
    }
}
