package com.gridnine.testing.filter.predetermined;

import com.gridnine.testing.filter.FlightPredicated;
import com.gridnine.testing.model.Flight;

import java.time.LocalDateTime;

public class DepartureAfterCurrentTime implements FlightPredicated {
    @Override
    public boolean test(Flight flight) {
        LocalDateTime now = LocalDateTime.now();
        return flight.getSegments().stream()
                .anyMatch(segment -> segment
                        .getDepartureDate().isAfter(now));
    }
}
