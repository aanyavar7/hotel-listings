package server.common.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Data structure that defines information about a hotel.
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity // For JPA
@With
@Table(name = "HOTEL") // For saving schema
public class Hotel {
    @Id
    Long id;

    /**
     * The name of the type of server.hotel (ex: Marriot, Hilton, etc.)
     */
    @Column(length = 3)
    String chainName;

    /**
     *
     */
    String hotelName;

    /**
     * The server.hotel street address
     */
    String street;

    /**
     * The server.hotel city
     */
    String city;

    /**
     * The server.hotel state
     */
    String state;

    /**
     * The server.hotel zip code
     */
    String zipcode;

    /**
     * The server.hotel country
     */
    String country;

    /**
     * The list of rooms at each server.hotel
     */
    @OneToMany(mappedBy = "hotel")
    List<Room> rooms;
}
