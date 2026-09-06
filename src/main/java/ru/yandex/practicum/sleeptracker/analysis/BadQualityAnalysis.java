package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysis;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class BadQualityAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long count = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult("Количество плохих сессий сна", count);
    }
}
