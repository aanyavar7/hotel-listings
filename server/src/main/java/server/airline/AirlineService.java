package server.airline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import server.common.model.Flight;
import server.common.model.FlightRequest;

/**
 * Airline service.
 */
@Service
public class AirlineService {
    @Autowired
    AirlineRepository repository;

    /**
     * Find all flights that match the passed {@link FlightRequest} fields.
     *
     * @param flightRequest Information about the flight, e.g., departure
     *                    date and departure/arrival airports.
     * @return A list containing all matching {@link Flight} objects.
     */
    public List<Flight> findFlights(FlightRequest flightRequest) {
        return repository.findByDepartureAirportAndDepartureDateAndArrivalAirportAndArrivalDate(
                flightRequest.getDepartureAirport(),
                flightRequest.getDepartureDate(),
                flightRequest.getArrivalAirport(),
                flightRequest.getArrivalDate());
    }

    /**
     * Find all flights in the airlines database.
     *
     * @return A list containing all matching flights.
     */
    public List<Flight> findAllFlights() {
        return repository.findAll();
    }

    /**
     * Finds all departure dates that have at least one flight
     * running from the departure airport to the arrival airport.
     *
     * @param departureAirport departure airport
     * @param arrivalAirport   arrival airport
     * @return A list of all matching departure dates.
     */
    public List<LocalDate> findDepartureDates(
            String departureAirport,
            String arrivalAirport) {
        return repository.findDepartureDates(departureAirport, arrivalAirport);
    }
}