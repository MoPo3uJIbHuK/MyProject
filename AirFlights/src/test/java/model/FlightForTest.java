package model;

import com.gridnine.testing.model.Segment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean that represents a flight.
 */
public class FlightForTest {
    private final List<SegmentForTest> segments;

    FlightForTest(final List<SegmentForTest> segs) {
        segments = segs;
    }

    public List<SegmentForTest> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}
