package com.gridnine.testing.filter.predetermined;

import com.gridnine.testing.filter.FlightPredicated;
import com.gridnine.testing.model.Flight;

public class DepartureBeforeArrives implements FlightPredicated {
    @Override
    public boolean test(Flight flight) {
        return flight.getSegments().stream()
                .anyMatch(segment -> segment.getDepartureDate()
                        .isBefore(segment.getArrivalDate()));
    }
}
