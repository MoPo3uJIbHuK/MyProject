package com.gridnine.testing.filter.predetermined;

import com.gridnine.testing.filter.FlightPredicated;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class WaitingFlightLessTwoHours implements FlightPredicated {
    @Override
    public boolean test(Flight flight) {
        List<Segment> segmentList = flight.getSegments();
        if (segmentList.size()>1) {
            long diff = 0;
            for (int i = 0; i <segmentList.size()-1 ; i++) {
                LocalDateTime arrivalCurrent = segmentList.get(i).getArrivalDate();
                LocalDateTime departureNext = segmentList.get(i+1).getDepartureDate();
                diff += Duration.between(arrivalCurrent,departureNext).getSeconds();
                if (diff>7200) {
                    return false;
                }
            }
        }
        return true;
    }
}
