package server.common.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

/**
 * Data structure that defines a request for a flight.
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@With
@Builder
public class FlightRequest {
    LocalDate departureDate;
    LocalDate arrivalDate;
    String departureAirport;
    String arrivalAirport;
    Integer passengers;
    String currency;
}
