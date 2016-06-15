package edu.iis.mto.bdd.trains.cucumber.steps;

import cucumber.api.Transform;
import cucumber.api.java.pl.Gdy;
import cucumber.api.java.pl.I;
import cucumber.api.java.pl.Wtedy;
import cucumber.api.java.pl.Zakładając;
import edu.iis.mto.bdd.trains.services.InMemoryTimetableService;
import org.joda.time.LocalTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by piotr on 08.06.16.
 */
public class ArrivalEstimationSteps {
    InMemoryTimetableService timetableService = new InMemoryTimetableService();

    private String line;
    private String destination;
    private String currentLocation;
    private LocalTime departureTime;

    private LocalTime actualArrivalTime;

    @Zakładając("że chcę się dostać z (.*) do (.*)$")
    public void givenLocationAndDestination(String currentLocation, String destination) throws Throwable {
        this.destination = destination;
        this.currentLocation = currentLocation;
    }

    @I("^następny pociąg odjeżdża o (.*) na linii (.*)$")
    public void andNextTrainDepartureTimeAndLine(@Transform(JodaLocalTimeConverter.class) LocalTime departureTime, String line) throws Throwable {
        this.line = line;
        this.departureTime = departureTime;
        timetableService.scheduleArrivalTime(line, departureTime, currentLocation);
    }

    @Gdy("^zapytam o godzinę przyjazdu$")
    public void whenIaskAboutArrivalTime() throws Throwable {
        actualArrivalTime = timetableService.getArrivalTime(line);
    }

    @Wtedy("^powinienem uzyskać następujący szacowany czas przyjazdu: (.*)$")
    public void shouldBeInformedAboutEstimatedArrivalTime(@Transform(JodaLocalTimeConverter.class) LocalTime expectedArrivalTime) throws Throwable {
        assertThat(actualArrivalTime, equalTo(expectedArrivalTime));
    }
}
