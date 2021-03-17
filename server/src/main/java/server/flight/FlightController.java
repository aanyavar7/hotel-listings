package server.flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import server.common.model.Airport;
import server.common.model.Flight;
import server.common.model.FlightRequest;

import static server.common.Constants.EndPoint.AIRPORTS;
import static server.common.Constants.EndPoint.BEST_PRICE;
import static server.common.Constants.EndPoint.FLIGHTS;
import static server.common.Constants.EndPoint.FLIGHT_DATES;

@RestController
public class FlightController {
    @Autowired
    FlightService flightService;

    /**
     * Finds the list of all known airports.
     *
     * @return A list of {@code Airport} objects.
     */
    @GetMapping(AIRPORTS)
    public List<Airport> findAirports() {
        return flightService.findAirports();
    }

    /**
     * Find all flights that match the {@link FlightRequest} requirements.
     *
     * @param flightRequest contains all parameters to match.
     * @return A list of matching {@link Flight} objects.
     */
    @PostMapping(FLIGHTS)
    public List<Flight> findFlights(@RequestBody FlightRequest flightRequest) {
        return flightService.findFlights(flightRequest);
    }

    /**
     * Find all flights from all airlines.
     *
     * @return A list of matching {@link Flight} objects from all airlines.
     */
    @GetMapping(FLIGHTS)
    public List<Flight> findAllFlights() {
        return flightService.findAllFlights();
    }

    /**
     * Finds the best flight price for the passed {@link FlightRequest}
     * parameter.
     *
     * @param flightRequest The flight to price
     * @return A {@link Flight} containing the best price
     */
    @PostMapping(BEST_PRICE)
    public Flight findBestPrice(@RequestBody FlightRequest flightRequest) {
        return flightService.findBestPrice(flightRequest);
    }

    /**
     * Finds all departure dates that have at least one flight
     * running from the departure airport to the arrival airport.
     *
     * @param departureAirport departure airport
     * @param arrivalAirport   arrival airport
     * @return A list of all matching dates.
     */
    @GetMapping(FLIGHT_DATES)
    public List<LocalDate> findDepartureDates(
            @RequestParam String departureAirport,
            @RequestParam String arrivalAirport) {
        return flightService.findDepartureDates(departureAirport, arrivalAirport);
    }
}