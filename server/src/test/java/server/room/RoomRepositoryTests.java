package server.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import server.common.model.Hotel;
import server.common.model.Room;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Ensure that app does not create and populate repositories.
@DataJpaTest(properties = {"spring.datasource.data="})
public class RoomRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository repository;

    List<Room> rooms;

    @BeforeEach
    public void beforeEach() {
        entityManager.clear();
    }

    @Test
    public void testFindAllRooms() {
        List<Room> emptyRooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                "Bal Harbour", "FL", "33154", "USA", emptyRooms);
        String currency = "USD";

        rooms = new ArrayList<>();
        Room room1 = new Room(null, 2, "king", 570.0, currency, hotel);
        rooms.add(room1);
        Room room2 = new Room(null, 1, "queen", 240.0, currency, hotel);
        rooms.add(room2);
        Room room3 = new Room(null, 3, "double-double", 620.0, currency, hotel);
        rooms.add(room3);

        for (Room room : rooms) {
            entityManager.persist(room);
        }

        entityManager.flush();

        assertThat(repository.findAll()).isEqualTo(rooms);
    }

    @Test
    public void testFindByRoomCode() {
        List<Room> emptyRooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                "Bal Harbour", "FL", "33154", "USA", emptyRooms);
        String currency = "USD";

        rooms = new ArrayList<>();
        Room room1 = new Room(null, 2, "king", 570.0, currency, hotel);
        rooms.add(room1);
        Room room2 = new Room(null, 1, "queen", 240.0, currency, hotel);
        rooms.add(room2);
        Room room3 = new Room(null, 3, "double-double", 620.0, currency, hotel);
        rooms.add(room3);

        for (Room room : rooms) {
            entityManager.persist(room);
        }

        entityManager.flush();

        List<Room> results = repository.findByRoomCode("queen");
        assertEquals(List.of(rooms.get(1)), results);
//        assertThat(repository.findAll()).isEqualTo(rooms);
    }
}