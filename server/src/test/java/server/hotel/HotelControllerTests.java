package server.hotel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import server.common.model.Hotel;
import server.common.model.Room;
import server.room.RoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static server.common.Constants.EndPoint.*;

@SpringBootTest(properties = {"spring.config.name="})
@AutoConfigureMockMvc
@WithMockUser(username="user",roles={"USER"})
//@Ignore
public class HotelControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void testFindHotels() throws Exception {
//        List<Room> rooms = new ArrayList<>();
//        List<Hotel> hotels = List.of(
//                new Hotel(1, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
//                        "Bal Harbour", "FL", "33154", "USA", rooms),
//                new Hotel(2, "MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW",
//                        "Atlanta", "GA", "30305", "USA", rooms),
//                new Hotel(3, "MAR", "JW Marriott", "201 8th Ave S",
//                        "Nashville", "TN", "37203", "USA", rooms),
//                new Hotel(4, "HLT", "Hampton Inn by Hilton", "310 4th Ave S",
//                        "Nashville", "TN", "37201", "USA", rooms)
//        );
//
//        for (Hotel expected : hotels) {
//            //pull values from the one hotel i created to pass into findHotels
//
//            List<Hotel> expectedList = new ArrayList<>(1);
//            expectedList.add(expected);
//
//            when(hotelService.findHotels().
//                    thenReturn(expectedList);
//
//            String jsonResult = mockMvc.perform(
//                    MockMvcRequestBuilders
//                            .get("/" + HOTELS)
//                            .queryParam("city", expected.getCity())
//                            .queryParam("state", expected.getState())
//                            .queryParam("country", expected.getCountry()))
//                    .andExpect(status().isOk())
//                    .andReturn()
//                    .getResponse()
//                    .getContentAsString();
//
//            List<Hotel> result = objectMapper.readValue(
//                    jsonResult, new TypeReference<>() {
//                    });
//
//            List<Hotel> cleanedResult = result.stream()
//                    .map(hotel -> hotel.withId(null))
//                    .collect(Collectors.toList());
//
//            verify(hotelService, times(1)).findHotels(roomRequest);
//
//            assertThat(cleanedResult).isEqualTo(expectedList);
//
//            clearInvocations(hotelService);
//        }
//    }

    @Test
    public void testFindHotelsByLocation() throws Exception {
        List<Room> rooms = new ArrayList<>();
        List<Hotel> hotels = List.of(
                new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                        "Bal Harbour", "FL", "33154", "USA", rooms),
                new Hotel(2L, "MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW",
                        "Atlanta", "GA", "30305", "USA", rooms),
                new Hotel(3L, "MAR", "JW Marriott", "201 8th Ave S",
                        "Nashville", "TN", "37203", "USA", rooms),
                new Hotel(4L, "HLT", "Hampton Inn by Hilton", "310 4th Ave S",
                        "Nashville", "TN", "37201", "USA", rooms)
        );

        for (Hotel expected : hotels) {
            //pull values from the one hotel i created to pass into findHotels

            List<Hotel> expectedList = new ArrayList<>(1);
            expectedList.add(expected);

            when(hotelService.findHotelsByLocation(
                    expected.getCity(),
                    expected.getState(),
                    expected.getCountry()))
                    .thenReturn(expectedList);

            String jsonResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .get("/" + HOTEL_LOCATIONS)
                            .queryParam("city", expected.getCity())
                            .queryParam("state", expected.getState())
                            .queryParam("country", expected.getCountry()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            List<Hotel> result = objectMapper.readValue(
                    jsonResult, new TypeReference<>() {
                    });

            List<Hotel> cleanedResult = result.stream()
//                    .map(hotel -> hotel.withId(null))
                    .collect(Collectors.toList());

            verify(hotelService, times(1))
                    .findHotelsByLocation(expected.getCity(), expected.getState(), expected.getCountry());

            assertThat(cleanedResult).isEqualTo(expectedList);

            clearInvocations(hotelService);
        }
    }

    @Test
    public void testFindRooms() throws Exception {
        List<Room> emptyRooms = new ArrayList<>();
        Hotel hotel = new Hotel(4L, "HLT", "Hampton Inn by Hilton", "310 4th Ave S",
                "Nashville", "TN", "37201", "USA", emptyRooms);
        List<Room> rooms = new ArrayList<>();
        Room room1 = Room.builder()
                .guests(2)
                .roomCode("king")
                .price(570.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        rooms.add(room1);
        Room room2 = Room.builder()
                .guests(1)
                .roomCode("queen")
                .price(240.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        rooms.add(room2);
        Room room3 = Room.builder()
                .guests(3)
                .roomCode("double-double")
                .price(620.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        rooms.add(room3);

        for (Room expected : rooms) {

            List<Room> expectedList = new ArrayList<>(1);
            expectedList.add(expected);

            when(roomService.findByRoomCode(expected.getRoomCode()))
                    .thenReturn(expectedList);

            String jsonResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .get("/" + ROOM_CODES)
                            .queryParam("roomCode", expected.getRoomCode()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            List<Room> result = objectMapper.readValue(
                    jsonResult, new TypeReference<>() {
                    });

            List<Room> cleanedResult = result.stream()
                    .map(room -> room.withId(null))
                    .collect(Collectors.toList());

            verify(roomService, times(1)).findByRoomCode(expected.getRoomCode());

            assertThat(cleanedResult).isEqualTo(expectedList);

            clearInvocations(roomService);
        }
    }

    @Test
    public void testFindBestPrice() throws Exception {
        List<Room> emptyRooms = new ArrayList<>();
        Hotel hotel = new Hotel(4L, "HLT", "Hampton Inn by Hilton", "310 4th Ave S",
                "Nashville", "TN", "37201", "USA", emptyRooms);
//        List<Hotel> hotels = List.of(
//                new Hotel(1, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
//                        "Bal Harbour", "FL", "33154", "USA", rooms),
//                new Hotel(2, "MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW",
//                        "Atlanta", "GA", "30305", "USA", rooms),
//                new Hotel(3, "MAR", "JW Marriott", "201 8th Ave S",
//                        "Nashville", "TN", "37203", "USA", rooms),
//                new Hotel(4, "HLT", "Hampton Inn by Hilton", "310 4th Ave S",
//                        "Nashville", "TN", "37201", "USA", rooms)
//        );
        List<Room> rooms = new ArrayList<>();
        Room room1 = Room.builder()
                .guests(2)
                .roomCode("king")
                .price(570.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        rooms.add(room1);
        Room room2 = Room.builder()
                .guests(1)
                .roomCode("queen")
                .price(240.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        rooms.add(room2);
        Room room3 = Room.builder()
                .guests(3)
                .roomCode("double-double")
                .price(620.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        rooms.add(room3);

        for (Room expected : rooms) {

            List<Room> expectedList = new ArrayList<>(1);
            expectedList.add(expected);

            when(roomService.findBestPrice(expected.getRoomCode(), expected.getCurrency()))
                    .thenReturn(expectedList);

            String jsonResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .get("/" + BEST_PRICE)
                            .queryParam("roomCode", expected.getRoomCode())
                            .queryParam("currency", expected.getCurrency()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            List<Room> result = objectMapper.readValue(
                    jsonResult, new TypeReference<>() {
                    });

            List<Room> cleanedResult = result.stream()
                    .map(room -> room.withId(null))
                    .collect(Collectors.toList());

            verify(roomService, times(1))
                    .findBestPrice(expected.getRoomCode(), expected.getCurrency());

            assertThat(cleanedResult).isEqualTo(expectedList);

            clearInvocations(roomService);
        }
    }
}
