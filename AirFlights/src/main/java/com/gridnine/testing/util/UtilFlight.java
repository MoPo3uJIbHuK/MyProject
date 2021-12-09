package com.gridnine.testing.util;

import com.gridnine.testing.filter.FlightPredicated;
import com.gridnine.testing.model.Flight;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UtilFlight {

    public void printInfo(List<Flight> flightList,
                          FlightPredicated... predicate) {
        flightList.stream().filter(flight -> isFilterOut(flight, predicate))
                .forEach(System.out::println);
    }

    private boolean isFilterOut(Flight flight, FlightPredicated... predicated) {
        return Arrays.stream(predicated)
                .allMatch(predicate -> predicate.test(flight));
    }
}
