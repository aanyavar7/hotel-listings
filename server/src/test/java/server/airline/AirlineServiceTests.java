package server.airline;

import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import server.common.model.Flight;
import server.common.model.FlightRequest;

import static server.common.Constants.EndPoint.FLIGHT_DATES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Ensure that app does not create and populate repositories.
@SpringBootTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
@AutoConfigureMockMvc
// Create test table along with test data.
@Sql({"classpath:/airline/create.sql"})
public class AirlineServiceTests {

    @Autowired
    AirlineService airlineService;

    @Test
    public void testFindFlights() {
        List<Flight> expected = new ArrayList<>(2);
        LocalDate date = LocalDate.parse("2021-02-24");
        String departureAirport = "BTV";
        String arrivalAirport = "MOB";
        String currency = "USD";

        expected.add(
                Flight.builder()
                        .departureAirport(departureAirport)
                        .departureDate(date)
                        .departureTime(LocalTime.parse("11:44:00"))
                        .arrivalAirport(arrivalAirport)
                        .arrivalDate(date)
                        .arrivalTime(LocalTime.parse("17:51:00"))
                        .price(872.92)
                        .currency(currency)
                        .kilometers(1211)
                        .airlineCode("SWA")
                        .build()
        );
        expected.add(
                Flight.builder()
                        .departureAirport(departureAirport)
                        .departureDate(date)
                        .departureTime(LocalTime.parse("10:11:00"))
                        .arrivalAirport(arrivalAirport)
                        .arrivalDate(date)
                        .arrivalTime(LocalTime.parse("12:38:00"))
                        .price(447.87)
                        .currency(currency)
                        .kilometers(721)
                        .airlineCode("AA")
                        .build()
        );

        FlightRequest flightRequest = FlightRequest.builder()
                .departureAirport(departureAirport)
                .departureDate(date)
                .arrivalAirport(arrivalAirport)
                .arrivalDate(date)
                .currency(currency)
                .build();


        List<Flight> result = airlineService.findFlights(flightRequest);

        assertThat(result).hasSameSizeAs(expected);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).withId(null)).isEqualTo(expected.get(i));
        }
    }

    @Test
    public void testFindAllFlights() {
        List<Flight> result = airlineService.findAllFlights();

        // Just ensure that the service returns a non-empty list.
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    public void testFindFlightDates() {
        List<LocalDate> expected = new ArrayList<>(2);
        expected.add(LocalDate.parse("2021-02-24"));
        expected.add(LocalDate.parse("2021-02-25"));


        List<LocalDate> result = airlineService.findDepartureDates("BTV", "MOB");

        // Make sure the controller was to return a non-empty list.
        assertThat(result).isEqualTo(expected);
    }
}