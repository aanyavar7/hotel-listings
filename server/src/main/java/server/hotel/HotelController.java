package server.hotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.common.model.Hotel;
import server.common.model.Room;
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
     * Finds the list of all known hotels.
     *
     * @return A list of {@code Hotel} objects.
     */
    @GetMapping(HOTELS)
    public List<Hotel> findAllHotels() {
        return hotelService.findHotels();
    }

    /**
     * Find all hotels that match the parameter requirements.
     *
     * @param city city of desired hotel
     * @param state state of desired hotel
     * @param country country of desired hotel
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
     */
    @GetMapping(BEST_PRICE)
    List<Room> findBestPrice(
            @RequestParam String roomCode,
            @RequestParam String currency) {
        return roomService.findBestPrice(
                roomCode,
                currency);
    }

    /**
     * Find all rooms that match given room code
     *
     * @param roomCode the desired room code
     * @return A list of matching {@Link Room} objects
     */
    @GetMapping(ROOM_CODES)
    public List<Room> findRoomsByRoomCode(
            @RequestParam String roomCode) {
        return roomService.findByRoomCode(roomCode);
    }
}