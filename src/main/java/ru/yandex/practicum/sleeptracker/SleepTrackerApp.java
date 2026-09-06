package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analysis.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Укажите путь к файлу с логом.");
            return;
        }

        List<SleepingSession> sessions = Files.lines(Path.of(args[0]))
                .map(SleepTrackerApp::parseSession)
                .toList();

        List<SleepAnalysis> analyses = List.of(
                new SessionCountAnalysis(),
                new MinDurationAnalysis(),
                new MaxDurationAnalysis(),
                new AverageDurationAnalysis(),
                new BadQualityAnalysis(),
                new SleeplessNightsAnalysis(),
                new ChronotypeAnalysis()
        );

        analyses.stream()
                .map(analysis -> analysis.apply(sessions))
                .forEach(result -> System.out.println(result));
    }

    private static SleepingSession parseSession(String line) {
        String[] parts = line.split(";");

        return new SleepingSession(
                LocalDateTime.parse(parts[0], FORMATTER),
                LocalDateTime.parse(parts[1], FORMATTER),
                SleepQuality.valueOf(parts[2])
        );
    }
}