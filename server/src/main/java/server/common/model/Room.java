package server.common.model;

import lombok.*;

import javax.persistence.*;

/**
 * Data structure that defines a response for a flight, which is
 * returned by various microservices to indicate which flights
 * match a {@code FlightRequest}.
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@With
@Entity(name = "ROOM") // For JPA
@Table(name = "ROOM") // For saving schema
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //list of current reservations for the room
    //given the start and end date of each reservation
//    List<Pair<LocalDate, LocalDate>> reservations;
    //reservationDates -- needed as a table, not a pair^^
        // @Id id (per each reservation)
        // roomId (join relationship for the specific reservation to determine what room it is)
        // start date
        // end date

//    LocalDate checkInDate;
//    LocalDate checkOutDate;

    //determines whether the room is currently occupied
//    boolean inUse;

    //total number of guests that can stay in the room
    int guests;

    //type of room (ex: queen, king, etc.)
    String roomCode;

    //daily price of the room
    @Column(precision = 10, scale = 2)
    double price;

    //currency
    @Column(length = 3)
    String currency;

    //Hotel that the room is a part of
    @ManyToOne(cascade = CascadeType.ALL)
    Hotel hotel;

}