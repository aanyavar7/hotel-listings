package server.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

/**
 * Data structure that defines information about an airport.
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity // For JPA
@Table(name = "AIRPORT") // For saving schema
public class Airport {
    /**
     * The three-letter airport code is the unique primary key.
     */
    @Id
    @Column(length = 3)
    String airportCode;

    /**
     * The airport name (e.g., city, state, and country).
     */
    String airportName;
}
