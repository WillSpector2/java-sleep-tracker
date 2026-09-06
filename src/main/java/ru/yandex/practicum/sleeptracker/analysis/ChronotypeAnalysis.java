package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(this::isNightSession)
                .map(this::getChronotype)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long owl = counts.getOrDefault(Chronotype.OWL, 0L);
        long lark = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeon = counts.getOrDefault(Chronotype.PIGEON, 0L);

        long max = Math.max(owl, Math.max(lark, pigeon));

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

        return new SleepAnalysisResult("Хронотип", result);
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        return start.isAfter(LocalTime.of(18, 0))
                || end.isBefore(LocalTime.of(12, 0));
    }

    private Chronotype getChronotype(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        if (start.isAfter(LocalTime.of(23, 0))
                && end.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }

        if (start.isBefore(LocalTime.of(22, 0))
                && end.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}