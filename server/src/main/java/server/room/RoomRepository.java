package server.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.common.model.Room;

import java.util.List;

/**
 * Hotel Room repository.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
////    @Query
//    List<Hotel> findHotelsByRoomCode(
//            @Param("RoomCode") String roomCode
//    ); //to do this, put in hotel repository and use join
//
//    List<Room> findRoomsByRoomCode(
//            @Param("RoomCode") String roomCode
//    );

    List<Room> findByRoomCode(
            @Param("RoomCode") String roomCode
    );

    //can only query for rooms, not hotels

//    List<Room> findByCityAndStateAndCountry(
//            @Param("City") String city,
//            @Param("State") String state,
//            @Param("Country") String country
//    );
//
//    List<Room> findByHotel(
//            @Param("Hotel") String hotel
//    );
    //to get city, need an indirect query to get city to get server.hotel ids

    /**
     * A @Query annotated version of above method.
     */
//    @Query("from #{#entityName} " +
//            "where check_in_date = :checkInDate " +
//            "and check_out_date = :checkOutDate " +
//            "and hotel_id = :hotelId " +
//            "order by check_in_date asc, check_out_date asc")
//    List<Room> findMatching(
//            @Param("CheckInDate") LocalDate checkInDate,
//            @Param("CheckOutDate") LocalDate checkOutDate,
//            @Param("hotelId") String hotelId
//    );

}