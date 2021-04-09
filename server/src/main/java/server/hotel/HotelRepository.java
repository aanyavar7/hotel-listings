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
//    @Query("select * from HOTEL " +
//            "where city = :city" +
//            "and state = :state" +
//            "and country = :country")
    List<Hotel> findHotelsByCityAndStateAndCountry(
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country
   );

//    @Query("select h from HOTEL h " +
//            "where city = :city" +
//            "and state = :state" +
//            "and country = :country")
//    List<Room> findAvailableRoomsByCityAndStateAndCountryAndCheckInDateAndCheckOutDate(
//            @Param("city") String city,
//            @Param("state") String state,
//            @Param("country") String country,
//            @Param("checkInDate") LocalDate checkInDate,
//            @Param("checkOutDate") LocalDate checkOutDate
//    );
}