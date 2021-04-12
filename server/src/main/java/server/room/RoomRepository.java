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
    List<Room> findByRoomCode(
            @Param("RoomCode") String roomCode
    );
}