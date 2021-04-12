package server.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.common.model.Room;

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
     * Find all server.hotel rooms in the server.hotel room database.
     *
     * @return A list containing all matching server.hotel rooms.
     */
    public List<Room> findAllRooms() {
        return repository.findAll();
    }

    /**
     * Find all rooms that match the given room code in the room database
     *
     * @param roomCode the desired room code
     * @return A list containing all matching rooms
     */
    public List<Room> findByRoomCode(String roomCode) {
        return repository.findByRoomCode(roomCode);
    }

    /**
     * Find the best priced rooms that match the room code
     *
     * @param roomCode the desired room code
     * @param currency the currency you want it in
     * @return A list containing all the matching rooms
     */
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