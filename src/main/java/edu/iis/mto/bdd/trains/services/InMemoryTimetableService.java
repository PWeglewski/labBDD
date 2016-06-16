package edu.iis.mto.bdd.trains.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import edu.iis.mto.bdd.trains.model.Line;
import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryTimetableService implements TimetableService {

    List<Line> lines = ImmutableList.of(
            Line.named("Western").departingFrom("Emu Plains")
                    .withStations("Emu Plains", "Parramatta", "Town Hall", "North Richmond")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5), durationOf(5)),
            Line.named("Western").departingFrom("North Richmond")
                    .withStations("North Richmond", "Town Hall", "Parramatta", "Emu Plains")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5), durationOf(5)),
            Line.named("Epping").departingFrom("Epping")
                    .withStations("Epping", "Strathfield", "Central")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5)),
            Line.named("Epping").departingFrom("City")
                    .withStations("Central", "Strathfield", "Epping")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5)),
            Line.named("Newcastle").departingFrom("Epping")
                    .withStations("Epping", "Newcastle", "Central")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5)),
            Line.named("Newcastle").departingFrom("City")
                    .withStations("Central", "Newcastle", "Epping")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5)),
            Line.named("Northern").departingFrom("Epping")
                    .withStations("Epping", "Northern", "Central")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5)),
            Line.named("Northern").departingFrom("City")
                    .withStations("Central", "Northern", "Epping")
                    .withTransferTimes(durationOf(5), durationOf(5), durationOf(5))
    );

    // All trains leave the depots at the same time.
    List<LocalTime> universalDepartureTimes = ImmutableList.of(
            new LocalTime(7, 53), new LocalTime(7, 55), new LocalTime(7, 57),
            new LocalTime(8, 6), new LocalTime(8, 9), new LocalTime(8, 16));
    private Line currentLine;
    private LocalTime departureTime;
    private String startingPoint;

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
        this.startingPoint = startingPoint;
    }

    @Override
    public LocalTime getArrivalTime(String destination) {
        int startingPointIndex = getCurrentStationNumber(this.startingPoint);
        int destinationIndex = getFinalStationNumber(destination);

        Period duration = calculateTimeDifference(startingPointIndex, destinationIndex);
        LocalTime arrivalTime = new LocalTime(departureTime);
        arrivalTime.plusMinutes(Math.toIntExact(duration.getMinutes()));

        return arrivalTime;
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

    private Period durationOf(int minutes) {
        return new Period().plusMinutes(minutes);
    }

    private int getCurrentStationNumber(String startingPoint) {
        return currentLine.getStations().indexOf(startingPoint);
    }

    private int getFinalStationNumber(String destination) {
        return currentLine.getStations().indexOf(destination);
    }

    private Period calculateTimeDifference(int startIndex, int endIndex) {
        Period duration = new Period();
        for (int i = startIndex; i <= endIndex; i++) {
            duration.plus(currentLine.getTransferTimes().get(i));
        }
        return duration;
    }
}
