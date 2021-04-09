package server.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.common.model.Room;
import server.common.model.RoomRequest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hotel Room service.
 */
@Service
public class RoomService {
    @Autowired
    RoomRepository repository;

    /**
     * Find all server.hotel rooms that match the passed {@link RoomRequest} fields.
     *
     * @param roomRequest Information about the server.hotel room, e.g., check in
     *                    date and server.hotel city.
     * @return A list containing all matching {@link Room} objects.
     */
//    public List<Room> findHotelRooms(RoomRequest roomRequest) {
//        return repository.findByCityAndStateAndCountry(
//                roomRequest.getCity(),
//                roomRequest.getState(),
//                roomRequest.getCountry());
//    }

//    public List<Room> findByHotel(RoomRequest roomRequest){
//        return repository.findByHotel(roomRequest.get)
//    }

    /**
     * Find all server.hotel rooms in the server.hotel room database.
     *
     * @return A list containing all matching server.hotel rooms.
     */
    public List<Room> findAllRooms() {
        return repository.findAll();
    }

    public List<Room> findByRoomCode(String roomCode) {
        return repository.findByRoomCode(roomCode);
    }

//    public List<Room> findRoomsByRoomCode(String roomCode) {
//        return repository.findRoomsByRoomCode(roomCode);
//    }
//
//    public List<Hotel> findHotelsByRoomCode(String roomCode) {
//        return repository.findHotelsByRoomCode(roomCode);
//    }

//    public List<Room> findHotelRooms(RoomRequest roomRequest) {
//        return repository.findRoomsByRoomCode(roomRequest.getRoomCode());
//    }

    public List<Room> findBestPrice(String roomCode, String currency) {
        // Find the minimum price for the all matching flights.
        List<Room> rooms = findByRoomCode(roomCode);

        Double min = rooms
                // Convert to stream.
                .stream()

                // Find the minimum price
                .min(Comparator.comparingDouble(Room::getPrice))

                // Extract price field.
                .map(Room::getPrice)

                // Return null if no matching flights were found.
                .orElse(null);

        if (min == null) {
            return null;
        }

        // Find and return all flights with this minimum price.
        return rooms
                .stream()
                .filter(room -> room.getPrice() == min)
                .collect(Collectors.toList());
    }
}