package com.gridnine.testing;

import com.gridnine.testing.filter.predetermined.DepartureAfterCurrentTime;
import com.gridnine.testing.filter.predetermined.DepartureBeforeArrives;
import com.gridnine.testing.filter.predetermined.WaitingFlightLessTwoHours;
import com.gridnine.testing.model.FlightBuilder;
import com.gridnine.testing.util.UtilFlight;

public class Main {
    public static void main(String[] args) {
        UtilFlight utilFlight = new UtilFlight();
        //todo Использование утильного класса, согласно заданию
        System.out.println("Print flights with filters according to the task");
        utilFlight.printInfo(FlightBuilder.createFlights(), new DepartureAfterCurrentTime()
                , new DepartureBeforeArrives(), new WaitingFlightLessTwoHours());
        //todo Пример использования утильного класса
        System.out.println("Print flights without filter");
        utilFlight.printInfo(FlightBuilder.createFlights());
        //todo Пример использование утильного класса, с лямбда выражением
        System.out.println("Print  flights with once route");
        utilFlight.printInfo(FlightBuilder.createFlights()
                , (flight) -> (flight.getSegments().size() == 1));
    }
}
