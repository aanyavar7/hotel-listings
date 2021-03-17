package server.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import server.common.model.Flight;

/**
 * Airline repository.
 */
@Repository
public interface AirlineRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureAirportAndDepartureDateAndArrivalAirportAndArrivalDate(
            @Param("departureAirport") String departureAirport,
            @Param("departureDate") LocalDate departureDate,
            @Param("arrivalAirport") String arrivalAirport,
            @Param("arrivalDate") LocalDate arrivalDate
    );

    /**
     * A @Query annotated version of above method.
     */
    @Query("from #{#entityName} " +
            "where departureAirport = :departureAirport " +
            "and departureDate = :departureDate " +
            "and arrivalAirport = :arrivalAirport " +
            "and arrivalDate = :arrivalDate " +
            "order by departureTime asc, arrivalTime asc")
    List<Flight> findMatching(
            @Param("departureAirport") String departureAirport,
            @Param("departureDate") LocalDate departureDate,
            @Param("arrivalAirport") String arrivalAirport,
            @Param("arrivalDate") LocalDate arrivalDate
    );

    @Query("select DISTINCT departureDate from #{#entityName} " +
           "where departureAirport = :departureAirport " +
           "and arrivalAirport = :arrivalAirport " +
           "order by departureDate asc")
    List<LocalDate> findDepartureDates(
            @Param("departureAirport") String departureAirport,
            @Param("arrivalAirport") String arrivalAirport
    );
}