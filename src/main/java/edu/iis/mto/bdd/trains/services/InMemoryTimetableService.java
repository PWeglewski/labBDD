package edu.iis.mto.bdd.trains.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import edu.iis.mto.bdd.trains.model.Line;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryTimetableService implements TimetableService {

    List<Line> lines = ImmutableList.of(
            Line.named("Western").departingFrom("Emu Plains")
                    .withStations("Emu Plains", "Parramatta", "Town Hall", "North Richmond"),
            Line.named("Western").departingFrom("North Richmond")
                    .withStations("North Richmond", "Town Hall", "Parramatta", "Emu Plains"),
            Line.named("Epping").departingFrom("Epping")
                    .withStations("Epping", "Strathfield", "Central"),
            Line.named("Epping").departingFrom("City")
                    .withStations("Central", "Strathfield", "Epping"),
            Line.named("Newcastle").departingFrom("Epping")
                    .withStations("Epping", "Newcastle", "Central"),
            Line.named("Newcastle").departingFrom("City")
                    .withStations("Central", "Newcastle", "Epping"),
            Line.named("Northern").departingFrom("Epping")
                    .withStations("Epping", "Northern", "Central"),
            Line.named("Northern").departingFrom("City")
                    .withStations("Central", "Northern", "Epping")
    );
    // All trains leave the depots at the same time.
    List<LocalTime> universalDepartureTimes = ImmutableList.of(
            new LocalTime(7, 53), new LocalTime(7, 55), new LocalTime(7, 57),
            new LocalTime(8, 6), new LocalTime(8, 9), new LocalTime(8, 16));
    private Line currentLine;
    private LocalTime departureTime;

    @Override
    public List<LocalTime> findArrivalTimes(Line line, String targetStation) {
        Line targetLine = lineMatching(line);
        int timeTaken = 0;
        for (String station : targetLine.getStations()) {
            if (station.equals(targetStation)) {
                break;
            }
            timeTaken += 5;
        }
        List<LocalTime> arrivalTimes = Lists.newArrayList();
        for (LocalTime time : universalDepartureTimes) {
            arrivalTimes.add(time.plusMinutes(timeTaken));
        }
        return arrivalTimes;
    }

    private Line lineMatching(Line requestedLine) {
        for (Line line : lines) {
            if (line.equals(requestedLine)) {
                return line;
            }
        }
        return null;
    }

    @Override
    public List<Line> findLinesThrough(String departure, String destination) {
        //return ImmutableList.of(lines.get(0));
        List<Line> resultLines = new ArrayList<Line>();
        for (Line line : lines) {
            if (line.getStations().contains(departure) && line.getStations().contains(destination)) {
                if (line.getStations().indexOf(destination) > line.getStations().indexOf(departure)) {
                    resultLines.add(line);
                }
            }
        }
        return resultLines;
    }

    @Override
    public void scheduleArrivalTime(String line, LocalTime departureTime, String startingPoint) {
        this.currentLine = findLine(line, startingPoint);
        this.departureTime = departureTime;
    }

    @Override
    public LocalTime getArrivalTime(String destination) {
        // TODO: Call the back-end service to retrieve this data
        return null;
    }

    private Line findLine(String lineName, String station) {
        Optional<Line> optionalLine = lines.stream()
                .filter(line -> lineName.equals(line.getLine()))
                .filter(line -> lineMatchAnyStation(line, station))
                .findFirst();
        if (optionalLine.isPresent()) return optionalLine.get();
        else return null;
    }

    private boolean lineMatchAnyStation(Line line, String station) {
        return line.getDepartingFrom() == station || line.getStations().contains(station);
    }
}
