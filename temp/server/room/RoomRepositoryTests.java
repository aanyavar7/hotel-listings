package server.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import server.common.model.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

// Ensure that app does not create and populate repositories.
@DataJpaTest/*(properties = {"spring.datasource.schema=", "spring.datasource.data="})*/
public class RoomRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository repository;

    List<Room> rooms;

    private static final int HOTEL_BRAND_COUNT = 2;
    private static final int HOTEL_COUNT = 6;
    private static final int ROOM_COUNT_PER_HOTEL = 1;

    private static final int EXPECTED_AVAILABLE_ROOMS_PER_DAY =
            HOTEL_COUNT * ROOM_COUNT_PER_HOTEL;

    @BeforeEach
    public void beforeEach() {
        rooms = new ArrayList<>();
        //create 2/3 server.hotel stays myself instead of using the random factory
        LocalDate date = LocalDate.parse("2021-03-24");
        LocalDate date2 = date.plusDays(5);
        Room room1 = Room.builder()
                .checkInDate(date)
                .checkOutDate(date2)
                .guests(2)
                .roomCode("king")
                .price(570.0)
                .currency("USD")
                .hotelId("MAR")
                .build();
        rooms.add(room1);

        date = LocalDate.parse("2021-03-25");
        date2 = date.plusDays(2);
        Room room2 = Room.builder()
                .checkInDate(date)
                .checkOutDate(date2)
                .guests(1)
                .roomCode("queen")
                .price(240.0)
                .currency("USD")
                .hotelId("HLT")
                .build();
        rooms.add(room2);

        date = LocalDate.parse("2021-03-21");
        date2 = date.plusDays(3);
        Room room3 = Room.builder()
                .checkInDate(date)
                .checkOutDate(date2)
                .guests(3)
                .roomCode("double-double")
                .price(620.0)
                .currency("USD")
                .hotelId("MAR")
                .build();
        rooms.add(room3);

        for (Room room : rooms) {
            entityManager.persist(room);
        }

        entityManager.flush();
    }

    @Test
    public void testFindByCheckInDateAndCheckOutDateAndHotelId() {
        for (Room room : rooms) {
            List<Room> response =
                    repository.findByCheckInDateAndCheckOutDateAndHotelId(
                            room.getCheckInDate(),
                            room.getCheckOutDate(),
                            room.getHotelId()
                    );
            assertThat(response.get(0)).isEqualTo(room);
        }
    }

    @Test
    public void testFindHotelStayDates() {
        List<LocalDate> expected =
                rooms.stream()
                        .filter(room ->
                                "MAR".equals(room.getHotelId()))
                        .map(Room::getCheckInDate)
                        .collect(Collectors.toList());

        List<Room> result = repository.findCheckInDate("MAR");

        // Make sure the controller was to return a non-empty list.
        assertThat(result).isEqualTo(expected);
    }
}