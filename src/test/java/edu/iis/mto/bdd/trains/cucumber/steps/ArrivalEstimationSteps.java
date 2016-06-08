package edu.iis.mto.bdd.trains.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.Transform;
import cucumber.api.java.pl.Gdy;
import cucumber.api.java.pl.I;
import cucumber.api.java.pl.Wtedy;
import cucumber.api.java.pl.Zakładając;
import org.joda.time.LocalTime;

/**
 * Created by piotr on 08.06.16.
 */
public class ArrivalEstimationSteps {
    @Zakładając("^, że chcę się dostać z (.*) do (.*)$")
    public void givenLocationAndDestination(String currentLocation, String destination) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @I("^następny pociąg odjeżdża o (.*) na linii (.*)$")
    public void andNextTrainDepartureTimeAndLine(@Transform(JodaLocalTimeConverter.class) LocalTime departureTime, String line) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Gdy("^zapytam o godzinę przyjazdu$")
    public void whenIaskAboutArrivalTime() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wtedy("^powinienem uzyskać następujący szacowany czas przyjazdu: (.*)$")
    public void shouldBeInformedAboutEstimatedArrivalTime(@Transform(JodaLocalTimeConverter.class) LocalTime arrivalTime) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
