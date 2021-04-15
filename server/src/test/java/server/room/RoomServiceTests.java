package server.room;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.common.model.Hotel;
import server.common.model.Room;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// Ensure that app does not create and populate repositories.
@SpringBootTest(properties = {"spring.datasource.data="})
public class RoomServiceTests {

    @MockBean
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

//    @Test
//    public void testFindHotels() {
//        List<Room> expected = new ArrayList<>(2);
//        String currency = "USD";
//        String city = "Bal Harbour";
//        String state = "FL";
//        String country = "USA";
//        List<Room> rooms = new ArrayList<>();
//        Hotel hotel = new Hotel(1, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
//                "Bal Harbour", "FL", "33154", "USA", rooms);
//
//        expected.add(
//                Room.builder()
//                        .guests(2)
//                        .roomCode("twin")
//                        .price(532.74)
//                        .currency(currency)
//                        .hotel(hotel)
//                        .build()
//        );
//        expected.add(
//                Room.builder()
//                        .guests(2)
//                        .roomCode("king")
//                        .price(872.00)
//                        .currency(currency)
//                        .hotel(hotel)
//                        .build()
//        );
//
//        RoomRequest roomRequest = RoomRequest.builder()
//                .city(city)
//                .state(state)
//                .country(country)
//                .guests(2)
//                .roomCode("queen")
//                .build();
//
//
//        List<Room> result = roomService.findByRoomCode(expected.get(0).getRoomCode());
//
//        assertThat(result).hasSameSizeAs(expected.get(0));
//        for (int i = 0; i < result.size(); i++) {
//            assertThat(result.get(i).withId(null)).isEqualTo(expected.get(i));
//        }
//    }

    @Test
    public void testFindAllRooms() {
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                "Bal Harbour", "FL", "33154", "USA", rooms);
        List<Room> expected = List.of(
                new Room(null, 3, "double-double", 700.0, "USD", hotel),
                new Room(null, 2, "twin", 750.0, "USD", hotel),
                new Room(null, 2, "queen", 240.0, "USD", hotel),
                new Room(null, 1, "queen", 360.0, "USD", hotel)
        );

        when(roomRepository.findAll()).thenReturn(expected);

        List<Room> result = roomService.findAllRooms();

        assertThat(result).isEqualTo(expected);
        verify(roomRepository, times(1)).findAll();

        // Just ensure that the service returns a non-empty list.
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    public void testFindByRoomCode() {
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                "Bal Harbour", "FL", "33154", "USA", rooms);
        List<Room> expected = List.of(
                new Room(null, 3, "double-double", 700.0, "USD", hotel),
                new Room(null, 2, "twin", 750.0, "USD", hotel),
                new Room(null, 2, "queen", 240.0, "USD", hotel),
                new Room(null, 1, "queen", 360.0, "USD", hotel)
        );

        when(roomRepository.findByRoomCode("queen")).thenReturn(expected);

        List<Room> result = roomService.findByRoomCode("queen");

        assertThat(result).isEqualTo(expected);
        verify(roomRepository, times(1)).findByRoomCode("queen");

        // Just ensure that the service returns a non-empty list.
        assertThat(result.size()).isGreaterThan(0);
    }
}