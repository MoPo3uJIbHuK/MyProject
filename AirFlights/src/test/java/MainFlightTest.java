import com.gridnine.testing.filter.predetermined.DepartureAfterCurrentTime;
import com.gridnine.testing.filter.predetermined.DepartureBeforeArrives;
import com.gridnine.testing.filter.predetermined.WaitingFlightLessTwoHours;
import com.gridnine.testing.model.FlightBuilder;
import com.gridnine.testing.util.UtilFlight;
import model.FlightBuilderForTest;
import model.FlightForTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MainFlightTest {
    private final UtilFlight utilFlight = new UtilFlight();
    private final PrintStream defaultPrintStream = System.out;
    private static final String LS = System.lineSeparator();
    private ByteArrayOutputStream output;
    private List<FlightForTest> testList;

    @BeforeEach
    void init() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        testList = new ArrayList<>(FlightBuilderForTest.createFlights());
    }

    @AfterEach
    void destroy() {
        System.setOut(defaultPrintStream);
    }

    @Test()
    @DisplayName("Отображаем рейсы которые вылетают после текущего времени")
    void assertDepartureAfterCurrentTime() {
        utilFlight.printInfo(FlightBuilder.createFlights()
                , new DepartureAfterCurrentTime());
        testList.remove(2);
        String expected = getStringToFlight(testList);
        assertEquals(expected, output.toString());
    }

    @Test()
    @DisplayName("Отображаем рейсы, где время вылета идёт после времени прилёта")
    void assertDepartureBeforeArrives(){
        utilFlight.printInfo(FlightBuilder.createFlights()
                , new DepartureBeforeArrives());
        testList.remove(3);
        String expected = getStringToFlight(testList);
        assertEquals(expected, output.toString());
    }
    @Test()
    @DisplayName("Отображаем рейсы, в которых пассажир в ожидании следующего рейса проводит меньше двух часов")
    void assertWaitingFlightLessTwoHours(){
        utilFlight.printInfo(FlightBuilder.createFlights()
                , new WaitingFlightLessTwoHours());
        testList.remove(5);
        testList.remove(4);
        String expected = getStringToFlight(testList);
        assertEquals(expected, output.toString());
    }
    @Test
    @DisplayName("Выполненение всех условий одновременно")
    void assertAllFilter(){
        utilFlight.printInfo(FlightBuilder.createFlights()
                , new DepartureAfterCurrentTime()
                , new DepartureBeforeArrives()
                , new WaitingFlightLessTwoHours());
        testList.remove(5);
        testList.remove(4);
        testList.remove(3);
        testList.remove(2);
        String expected = getStringToFlight(testList);
        assertEquals(expected, output.toString());
    }
    @Test()
    @DisplayName("Отображаем рейсы без пересадок")
    void assertFlightsWithOnceRoute(){
        utilFlight.printInfo(FlightBuilder.createFlights()
                , (flight) -> (flight.getSegments().size() == 1));
        testList.remove(5);
        testList.remove(4);
        testList.remove(1);
        String expected = getStringToFlight(testList);
        assertEquals(expected, output.toString());
    }
    @Test()
    @DisplayName("Отображаем все рейсы без параметров")
    void assertAllFlights(){
        utilFlight.printInfo(FlightBuilder.createFlights());
        String expected = getStringToFlight(testList);
        assertEquals(expected, output.toString());
    }

    private String getStringToFlight(List<FlightForTest> flightForTests) {
        StringBuilder sb = new StringBuilder();
        flightForTests.stream().forEach(flight -> sb.append(flight).append(LS));
        return sb.toString();
    }
}
