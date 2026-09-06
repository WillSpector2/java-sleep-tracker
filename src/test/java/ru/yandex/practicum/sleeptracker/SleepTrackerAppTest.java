package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepTrackerAppTest {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    @Test
    void shouldCountSessions() {
        assertEquals(5, new SessionCountAnalysis().apply(testSessions()).getValue());
    }

    @Test
    void shouldCountSessionsForTwoSessions() {
        assertEquals(
                2,
                new SessionCountAnalysis().apply(testSessions().subList(0, 2)).getValue()
        );
    }

    @Test
    void shouldFindMinimumDuration() {
        assertEquals(50L, new MinDurationAnalysis().apply(testSessions()).getValue());
    }

    @Test
    void shouldFindMinimumDurationForSingleSession() {
        assertEquals(
                500L,
                new MinDurationAnalysis().apply(List.of(
                        session("01.10.25 23:15", "02.10.25 07:35")
                )).getValue()
        );
    }

    @Test
    void shouldFindMaximumDuration() {
        assertEquals(500L, new MaxDurationAnalysis().apply(testSessions()).getValue());
    }

    @Test
    void shouldFindMaximumDurationForShortSessions() {
        assertEquals(
                120L,
                new MaxDurationAnalysis().apply(List.of(
                        session("01.10.25 23:00", "02.10.25 00:30"),
                        session("02.10.25 23:00", "03.10.25 01:00")
                )).getValue()
        );
    }

    @Test
    void shouldCalculateAverageDuration() {
        assertEquals(365.0, new AverageDurationAnalysis().apply(testSessions()).getValue());
    }

    @Test
    void shouldCalculateAverageDurationForTwoSessions() {
        assertEquals(
                60.0,
                new AverageDurationAnalysis().apply(List.of(
                        session("01.10.25 23:00", "02.10.25 00:00"),
                        session("02.10.25 23:00", "03.10.25 00:00")
                )).getValue()
        );
    }

    @Test
    void shouldCountBadQuality() {
        assertEquals(1L, new BadQualityAnalysis().apply(testSessions()).getValue());
    }

    @Test
    void shouldReturnZeroBadQualityWhenAllSessionsAreGood() {
        assertEquals(
                0L,
                new BadQualityAnalysis().apply(List.of(
                        session("01.10.25 23:00", "02.10.25 07:00"),
                        session("02.10.25 23:00", "03.10.25 07:00")
                )).getValue()
        );
    }

    @Test
    void shouldCountSleeplessNights() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:00", "02.10.25 03:00"),
                session("02.10.25 23:00", "03.10.25 03:00"),
                session("04.10.25 07:00", "04.10.25 11:00")
        );

        assertEquals(
                1L,
                new SleeplessNightsAnalysis().apply(sessions).getValue()
        );
    }

    @Test
    void shouldNotCountNightWhenSleepStartsAt2Am() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 02:00", "01.10.25 07:00")
        );

        assertEquals(
                0L,
                new SleeplessNightsAnalysis().apply(sessions).getValue()
        );
    }

    @Test
    void shouldCountDaytimeOnlyAsSleeplessNight() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 07:00", "01.10.25 11:00"),
                session("02.10.25 23:00", "03.10.25 03:00")
        );

        assertEquals(
                2L,
                new SleeplessNightsAnalysis().apply(sessions).getValue()
        );
    }

    @Test
    void shouldReturnZeroSleeplessNightsForNightSession() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:00", "02.10.25 06:00")
        );

        assertEquals(
                0L,
                new SleeplessNightsAnalysis().apply(sessions).getValue()
        );
    }

    @Test
    void shouldReturnOwlChronotype() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:30", "02.10.25 10:00")
        );

        assertEquals(
                Chronotype.OWL,
                new ChronotypeAnalysis().apply(sessions).getValue()
        );
    }

    @Test
    void shouldReturnLarkChronotype() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 21:00", "02.10.25 06:00")
        );

        assertEquals(
                Chronotype.LARK,
                new ChronotypeAnalysis().apply(sessions).getValue()
        );
    }

    @Test
    void shouldReturnPigeonForPigeonSession() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:30", "02.10.25 07:30")
        );

        assertEquals(
                Chronotype.PIGEON,
                new ChronotypeAnalysis().apply(sessions).getValue()
        );
    }

    private List<SleepingSession> testSessions() {
        return List.of(
                session("01.10.25 23:15", "02.10.25 07:30"),
                session("02.10.25 23:50", "03.10.25 06:40"),
                session("03.10.25 14:10", "03.10.25 15:00"),
                badSession("03.10.25 23:40", "04.10.25 08:00"),
                session("05.10.25 00:10", "05.10.25 06:20")
        );
    }

    private SleepingSession session(String start, String end) {
        return new SleepingSession(
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER),
                SleepQuality.GOOD
        );
    }

    private SleepingSession badSession(String start, String end) {
        return new SleepingSession(
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER),
                SleepQuality.BAD
        );
    }
}