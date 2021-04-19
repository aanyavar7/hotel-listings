package server.common.model;

import lombok.*;

/**
 * Data structure that defines a request for a room.
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@With
@Builder
public class RoomRequest {

    /**
     * The city you want to find a hotel/room in
     */
    String city;

    /**
     * The state you want to find a hotel/room in
     */
    String state;

    /**
     * The country you want to find a hotel/room in
     */
    String country;

    /**
     * The total number of guests
     */
    Integer guests;

    /**
     * type of room you want (ex: queen, king, etc.)
     */
    String roomCode;
}
