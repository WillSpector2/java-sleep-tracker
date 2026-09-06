package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysis;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class SessionCountAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult("Количество сессий сна", sessions.size());
    }
}
