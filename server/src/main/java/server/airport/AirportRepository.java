package server.airport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.common.model.Airport;

/**
 * Airport repository.
 */
@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
}