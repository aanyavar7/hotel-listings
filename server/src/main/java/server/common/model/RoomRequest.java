package server.common.model;

import lombok.*;

/**
 * Data structure that defines a request for a flight.
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@With
@Builder
public class RoomRequest {
    //city you want to find a hotel/room in
    String city;

    //state you want to find a hotel/room in
    String state;

    //country you want to find a hotel/room in
    String country;

    //total number of guests
    Integer guests;

    //type of room you want (ex: queen, king, etc.)
    String roomCode;
}
