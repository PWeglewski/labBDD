package edu.iis.mto.bdd.trains.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.Transform;
import cucumber.api.java.pl.Gdy;
import cucumber.api.java.pl.Wtedy;
import cucumber.api.java.pl.Zakładając;
import edu.iis.mto.bdd.trains.services.InMemoryTimetableService;
import edu.iis.mto.bdd.trains.services.TimetableService;
import org.joda.time.LocalTime;

import java.util.List;

public class OptimalItinerarySteps {
    private static String line;
    private static String lineStart;
    private static String departure;
    private static String destination;
    private static List<LocalTime> departureTimes;

    InMemoryTimetableService timetableService = new InMemoryTimetableService();

    @Zakładając("^pociągi linii \"(.*)\" z \"(.*)\" odjeżdżają ze stacji \"(.*)\" do \"(.*)\" o$")
    public void givenArrivingTrains(String line, String lineStart, String departure, String destination,
                                    @Transform(JodaLocalTimeConverter.class) List<LocalTime> departureTimes) {
        this.line = line;
        this.lineStart = lineStart;
        this.departure = departure;
        this.destination = destination;
        this.departureTimes = departureTimes;
    }

    @Gdy("^chcę podróżować z \"([^\"]*)\" do \"([^\"]*)\" o (.*)$")
    public void whenIWantToTravel(String departure, String destination, @Transform(JodaLocalTimeConverter.class) LocalTime startTime) {

    }

    @Wtedy("^powinienem uzyskać informację o pociągach o:$")
    public void shouldBeInformedAbout(@Transform(JodaLocalTimeConverter.class) List<LocalTime> expectedTrainTimes) {

    }
}
