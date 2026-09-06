package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysis;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class MaxDurationAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long max = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0);

        return new SleepAnalysisResult("Максимальная продолжительность сна (мин)", max);
    }
}
