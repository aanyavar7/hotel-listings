package server.hotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.common.model.Hotel;
import server.common.model.RoomRequest;

import java.util.List;

/**
 * Hotel service.
 */
@Service
public class HotelService {
    @Autowired
    private HotelRepository repository;

    public List<Hotel> findHotels() {
        return repository.findAll();
    }

    /**
     * Find all flights that match the passed {@link RoomRequest} fields.
     *
     * @param city
     * @param state
     * @param country
     * @return A list containing all matching {@link Hotel} objects.
     */
    public List<Hotel> findHotelsByLocation(String city, String state, String country) {
        return repository.findHotelsByCityAndStateAndCountry(
                city,
                state,
                country);
    }

    //change all mappings to Get, everything should have query params and no roomRequest needed
}