package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;

import java.util.function.Predicate;

@FunctionalInterface
public interface FlightPredicated extends Predicate<Flight> {
    @Override
    boolean test(Flight flight);

}
