package edu.vanderbilt.client.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * Data structure that defines a response for a flight, which is
 * returned by various microservices to indicate which flights
 * match a `FlightRequest`.
 */
data class Flight(
    var id: Long,
    var departureAirport: String,
    var departureDate: LocalDate,
    var departureTime: LocalTime,
    var arrivalAirport: String,
    var arrivalDate: LocalDate,
    var arrivalTime: LocalTime,
    var kilometers: Int,
    var price: Double,
    var currency: String,
    var airlineCode: String
)