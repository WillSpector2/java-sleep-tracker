package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysis;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalysis implements SleepAnalysis {

    private static final LocalTime NIGHT_SESSION_START = LocalTime.of(18, 0);
    private static final LocalTime NIGHT_SESSION_END = LocalTime.of(12, 0);

    private static final LocalTime OWL_START = LocalTime.of(23, 0);
    private static final LocalTime OWL_END = LocalTime.of(9, 0);

    private static final LocalTime LARK_START = LocalTime.of(22, 0);
    private static final LocalTime LARK_END = LocalTime.of(7, 0);

    private static final String DESCRIPTION = "Хронотип";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(this::isNightSession)
                .map(this::getChronotype)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long owl = counts.getOrDefault(Chronotype.OWL, 0L);
        long lark = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeon = counts.getOrDefault(Chronotype.PIGEON, 0L);

        long max = List.of(owl, lark, pigeon).stream()
                .max(Long::compareTo)
                .orElse(0L);

        long typesWithMax = List.of(owl, lark, pigeon).stream()
                .filter(count -> count == max)
                .count();

        Chronotype result;

        if (max == 0 || typesWithMax > 1) {
            result = Chronotype.PIGEON;
        } else if (owl == max) {
            result = Chronotype.OWL;
        } else if (lark == max) {
            result = Chronotype.LARK;
        } else {
            result = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult(DESCRIPTION, result);
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        return start.isAfter(NIGHT_SESSION_START)
                || end.isBefore(NIGHT_SESSION_END);
    }

    private Chronotype getChronotype(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        if (start.isAfter(OWL_START) && end.isAfter(OWL_END)) {
            return Chronotype.OWL;
        }

        if (start.isBefore(LARK_START) && end.isBefore(LARK_END)) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}
