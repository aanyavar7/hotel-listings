package server.flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import server.airline.AirlineService;
import server.airport.AirportService;
import server.common.model.Airport;
import server.common.model.ExchangeRate;
import server.common.model.Flight;
import server.common.model.FlightRequest;
import server.exchange.ExchangeService;

@Service
public class FlightService {
    @Autowired
    AirportService airportService;

    @Autowired
    AirlineService airlineService;

    @Autowired
    ExchangeService exchangeService;

    /**
     * Find all flights that match the {@code flightRequest} fields.
     *
     * @param flightRequest Information about the flight, e.g., departure
     *                    date and departure/arrival airports.
     * @return A list containing all matching {@link Flight} objects.
     */
    public List<Flight> findFlights(FlightRequest flightRequest) {
        return airlineService.findFlights(flightRequest);
    }

    /**
     * Find all flights from all airlines.
     *
     * @return A list of {@link Flight}s objects from all airlines.
     */
    public List<Flight> findAllFlights() {
        return airlineService.findAllFlights();
    }

    /**
     * Finds all know airports.
     *
     * @return A List of {@code Airport} objects.
     */
    public List<Airport> findAirports() {
        return airportService.findAirports();
    }

    /**
     * Finds the flight with the best price that matches fields
     * in the passed {@link FlightRequest} fields.
     *
     * @param flightRequest Information about the flight, e.g., departure
     *                    date and departure/arrival airports
     * @return The {@link Flight} with the lowest price or null if
     * no matching flights were found.
     */
    public Flight findBestPrice(FlightRequest flightRequest) {
        // Find all flights matching the specified flightRequest
        return findFlights(flightRequest)
                // Convert to stream.
                .stream()

                // Find the minimum price
                .min(Comparator.comparingDouble(Flight::getPrice))

                // Return the flight or null if none was found.
                .orElse(null);
    }

    /**
     * Finds the exchange rate that matches the {@code currencyConversion}
     * fields.
     *
     * @param fromCurrency The 3 letter currency to convert from.
     * @param toCurrency   The 3 letter currency to convert to.
     * @return An {@link ExchangeRate} object containing the passed
     * parameters along with an exchange rate.
     */
    public ExchangeRate findExchangeRate(String fromCurrency, String toCurrency) {
        return exchangeService.findExchangeRate(fromCurrency, toCurrency);
    }

    /**
     * Finds all departure dates that have at least one flight
     * running from the departure airport to the arrival airport.
     *
     * @param departureAirport departure airport
     * @param arrivalAirport   arrival airport
     * @return A list of all matching dates.
     */
    public List<LocalDate> findDepartureDates(String departureAirport, String arrivalAirport) {
        return airlineService.findDepartureDates(departureAirport, arrivalAirport);
    }
}