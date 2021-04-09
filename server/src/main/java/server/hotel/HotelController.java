package server.hotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.common.model.Hotel;
import server.common.model.Room;
import server.common.model.RoomRequest;
import server.room.RoomService;

import java.util.List;

import static server.common.Constants.EndPoint.*;

@RestController
public class HotelController {
    @Autowired
    HotelService hotelService;

    @Autowired
    RoomService roomService;

    /**
     * Finds the list of all known hotles.
     *
     * @return A list of {@code Hotel} objects.
     */
    @GetMapping(HOTELS)
    public List<Hotel> findAllHotels() {
        return hotelService.findHotels();
    }

    /**
     * Find all hotels that match the {@link RoomRequest} requirements.
     *
     * @param city
     * @param state
     * @param country
     * @return A list of matching {@link Hotel} objects.
     */
    @GetMapping(HOTEL_LOCATIONS)
    public List<Hotel> findHotelsByLocation(
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String country) {
        return hotelService.findHotelsByLocation(city, state, country);
    }

    /**
     * Find all rooms from all hotels.
     *
     * @return A list of matching {@link Room} objects from all hotels.
     */
    @GetMapping(ROOMS)
    public List<Room> findAllRooms() {
        return roomService.findAllRooms();
    }

    /**
     * Find the best priced rooms that match the room code
     *
     * @return All {@link Room} objects that match the
     * {@link RoomRequest} that share the lowest price.
     */
    @GetMapping(BEST_PRICE)
    List<Room> findBestPrice(
            @RequestParam String roomCode,
            @RequestParam String currency) {
        return roomService.findBestPrice(
                roomCode,
                currency);
    }

    @GetMapping(ROOM_CODES)
    public List<Room> findRoomsByRoomCode(
            @RequestParam String roomCode) {
        return roomService.findByRoomCode(roomCode);
    }
}