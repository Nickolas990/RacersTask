package com.ru.javarush.echo.NickolayMelnikov.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    public static void processFile(Path filePath) {
        if (!filePath.toString().endsWith(".result")) {
            System.out.printf("File %s is skipped", filePath);
            return;
        }
        List<LapData> lapData = new ArrayList<>();
        try {
            System.out.println(filePath);
            Files.readAllLines(filePath).forEach(s -> {
                String[] lineParts = s.split(";");
                String racerName = lineParts[0];
                int lapNumber = Integer.parseInt(lineParts[1]);
                LocalTime lapTime = LocalTime.parse(lineParts[2]);
                lapData.add(new LapData(racerName, lapNumber, lapTime));
            });
            Comparator<LapData> lapDataComparator = (o1,o2) -> {
                if (o1.getLapTime().equals(o2.getLapTime())) {
                    return Integer.compare(o2.getLapNumber(), o1.getLapNumber());
                } else {
                    return o1.getLapTime().compareTo(o2.getLapTime());
                }
            };

            List<LapData> bestLaps = lapData.stream().collect(Collectors.groupingBy(LapData::racerName, Collectors.maxBy((lapDataComparator))))
                    .values()
                    .stream()
                    .map(Optional::get)
                    .sorted(lapDataComparator)
                    .toList();

            bestLaps.forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String[] args) {
        if (args.length != 2 || args[0] == null || !args[0].equals("-workdir")) {
            System.err.println("Your first argument should starts with \"-workdir\"");
            System.exit(1);
        }

        String dirValue = args[1];
        Path dirPath = null;

        try {
            dirPath = Path.of(dirValue);

        } catch (InvalidPathException ex) {
            System.err.println("This path is invalid: " + dirValue);
            System.err.println("Error details: " + ex.getMessage());
            System.exit(2);
        }

        if (!Files.isDirectory(dirPath)) {
            System.err.println("Wrong directory!");
            System.exit(3);
        }

        try {
            if (!Files.newDirectoryStream(dirPath).iterator().hasNext()) {
             System.err.println("Directory is empty");
             System.exit(4);
            }
        } catch (IOException e) {
            System.err.println("Something wrong with that file");
            System.exit(5);
        }

        try {
            Files.newDirectoryStream(dirPath).forEach(Main::processFile);
        } catch (IOException e) {
            System.err.println("Process mistake while processing!");
            System.err.println("Error details: " + e.getMessage());
            System.exit(5);
        }


    }
}
