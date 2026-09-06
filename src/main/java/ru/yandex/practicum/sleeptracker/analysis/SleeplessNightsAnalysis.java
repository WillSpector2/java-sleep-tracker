package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysis;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsAnalysis implements SleepAnalysis {

    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDate firstNight = sessions.get(0).getStart().toLocalDate();

        if (!sessions.get(0).getStart().toLocalTime().isBefore(LocalTime.NOON)) {
            firstNight = firstNight.plusDays(1);
        }

        LocalDate lastNight = sessions.get(sessions.size() - 1).getEnd().toLocalDate();

        long result = LongStream.rangeClosed(
                        0,
                        java.time.temporal.ChronoUnit.DAYS.between(firstNight, lastNight))
                .mapToObj(firstNight::plusDays)
                .filter(night -> sessions.stream()
                        .noneMatch(session -> {
                            var nightStart = night.atStartOfDay();
                            var nightEnd = night.atTime(NIGHT_END);

                            return session.getStart().isBefore(nightEnd)
                                    && session.getEnd().isAfter(nightStart);
                        }))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", result);
    }
}