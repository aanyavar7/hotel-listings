package server.common.model;

import lombok.*;

import javax.persistence.*;

/**
 * Data structure that defines a response for a room, which is
 * returned by various microservices to indicate which rooms
 * match a {@code RoomRequest}.
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

    /**
     * The total number of guests that can stay in the room
     */
    int guests;

    /**
     * The type of room (ex: queen, king, etc.)
     */
    String roomCode;

    /**
     * The daily price of the room
     */
    @Column(precision = 10, scale = 2)
    double price;

    /**
     *
     */
    @Column(length = 3)
    String currency;

    /**
     * The hotel that the room is a part of
     */
    @ManyToOne(cascade = CascadeType.ALL)
    Hotel hotel;

}