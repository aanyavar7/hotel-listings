package server.room;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import server.common.model.Room;
import server.common.model.RoomRequest;

import static org.assertj.core.api.Assertions.assertThat;

// Ensure that app does not create and populate repositories.
@SpringBootTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
@AutoConfigureMockMvc
// Create test table along with test data.
@Sql({"classpath:/room/create.sql"})
public class RoomServiceTests {

    @Autowired
    RoomService roomService;

    @Test
    public void testFindHotelStays() {
        //LocalDate date = LocalDate.parse("2021-02-24");
        //        LocalDate date2 = date.plusDays(5);
        //        String hotelLocation = "ATL";
        //        String currency = "USD";
        //
        //        Room expected = Room.builder()
        //                .hotelLocation(hotelLocation)
        //                .checkInDate(date)
        //                .checkOutDate(date2)
        //                .guests(2)
        //                .roomCode("Single Bed")
        //                .price(532.74)
        //                .currency(currency)
        //                .hotelName("MAR")
        //                .build();


        List<Room> expected = new ArrayList<>(2);
        LocalDate date = LocalDate.parse("2021-02-24");
        LocalDate date2 = date.plusDays(5);
        String hotelId = "ATL";
        String currency = "USD";

        expected.add(
                Room.builder()
                        .hotelId(hotelId)
                        .checkInDate(date)
                        .checkOutDate(date2)
                        .guests(2)
                        .roomCode("Single Bed")
                        .price(532.74)
                        .currency(currency)
                        .build()
        );
        expected.add(
                Room.builder()
                        .hotelId(hotelId)
                        .checkInDate(date)
                        .checkOutDate(date2)
                        .guests(2)
                        .roomCode("Suite")
                        .price(872.00)
                        .currency(currency)
                        .build()
        );

        RoomRequest roomRequest = RoomRequest.builder()
                .checkInDate(date)
                .checkOutDate(date2)
                .hotelId("ATL")
                .guests(2)
                .roomCode("Suite")
                .build();


        List<Room> result = roomService.findHotelRooms(roomRequest);

        assertThat(result).hasSameSizeAs(expected);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).withId(null)).isEqualTo(expected.get(i));
        }
    }

    @Test
    public void testFindAllHotelStays() {
        List<Room> result = roomService.findAllHotelRooms();

        // Just ensure that the service returns a non-empty list.
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    public void testFindRoomDates() {
        List<LocalDate> expected = new ArrayList<>(2);
        expected.add(LocalDate.parse("2021-02-24"));
        expected.add(LocalDate.parse("2021-02-25"));


        List<LocalDate> result = roomService.findCheckInDates("ATL");

        // Make sure the controller was to return a non-empty list.
        assertThat(result).isEqualTo(expected);
    }
}