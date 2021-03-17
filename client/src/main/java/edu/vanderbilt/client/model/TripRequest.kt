package edu.vanderbilt.client.model

import java.time.LocalDate

/**
 * Data structure that defines a request for a flight.
 */
data class FlightRequest(
    var departureDate: LocalDate,
    var arrivalDate: LocalDate,
    var departureAirport: String,
    var arrivalAirport: String,
    var passengers: Int = 0,
    var currency: String = "USD"
)