package server.airline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import server.common.model.Flight;

import static org.assertj.core.api.Assertions.assertThat;

// Ensure that app does not create and populate repositories.
@DataJpaTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
public class AirlineRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirlineRepository repository;

    List<Flight> flights;

    private static final int AIRLINE_COUNT = 3;
    private static final int AIRPORT_COUNT = 4;
    private static final int NUMBER_OF_DAYS = 5;
    private static final int FLIGHTS_PER_DAY = 2;

    private static final int EXPECTED_FLIGHTS_PER_DAY =
            AIRLINE_COUNT * FLIGHTS_PER_DAY;

    @BeforeEach
    public void beforeEach() {
        flights = FlightFactory.builder()
                .airlines(AIRLINE_COUNT)
                .airports(AIRPORT_COUNT)
                .from(LocalDate.now())
                .to(LocalDate.now().plusDays(NUMBER_OF_DAYS))
                .dailyFlights(FLIGHTS_PER_DAY)
                .currency("USD")
                .build();

        for (Flight flight : flights) {
            entityManager.persist(flight);
        }

        entityManager.flush();
    }

    @Test
    public void testFindByDepartureAirport() {
        for (Flight flight : flights) {
            List<Flight> response =
                    repository.findByDepartureAirportAndDepartureDateAndArrivalAirportAndArrivalDate(
                            flight.getDepartureAirport(),
                            flight.getDepartureDate(),
                            flight.getArrivalAirport(),
                            flight.getArrivalDate()
                    );

            assertThat(response).isNotNull();

            // Expect 3 flights per (1 for each airline).
            assertThat(response).hasSize(EXPECTED_FLIGHTS_PER_DAY);
        }
    }

    @Test
    public void testFindFlightDates() {
        List<LocalDate> expected =
                flights.stream()
                        .filter(flight ->
                                "BTV".equals(flight.getDepartureAirport()) &&
                                "MOB".equals(flight.getArrivalAirport()))
                        .map(Flight::getDepartureDate)
                        .collect(Collectors.toList());

        List<LocalDate> result = repository.findDepartureDates("BTV", "MOB");

        // Make sure the controller was to return a non-empty list.
        assertThat(result).isEqualTo(expected);
    }
}