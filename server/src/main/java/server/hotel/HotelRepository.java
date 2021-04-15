package server.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.common.model.Hotel;

import java.util.List;

/**
 * Hotel repository.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {
    //Query to find available rooms based on city/state/country and available dates
    List<Hotel> findByCityAndStateAndCountry(
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country
   );
}