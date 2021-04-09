/*
package server.room;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import server.common.model.Hotel;
import server.common.model.Room;
import server.hotel.HotelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static server.common.Constants.EndPoint.*;

@SpringBootTest(properties = {
        "spring.jpa.properties.hibernate.show_sql=false",
        "spring.config.name="
})
@AutoConfigureMockMvc
public class RoomIntegrationTests {
    List<Room> rooms;
    Room currentRoom;
    List<Room> expected;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        roomRepository.deleteAll();

        List<Room> emptyRooms = new ArrayList<>();
        Hotel hotel = new Hotel(4L, "HLT", "Hampton Inn by Hilton", "310 4th Ave S",
                "Nashville", "TN", "37201", "USA", emptyRooms);
        rooms = new ArrayList<>();
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

        currentRoom = rooms.get(0);
        expected = rooms.stream()
                .filter(rooms -> currentRoom.getRoomCode()
                        .equals(rooms.getRoomCode()) &&
                        (currentRoom.getGuests() == rooms.getGuests()))
                .collect(Collectors.toList());
        roomRepository.saveAll(rooms);
    }

    @Test
//    @Sql({"classpath:/room/create.sql"})
    public void testFindRooms() throws Exception {
        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/" + ROOMS)
                        .queryParam("roomCode", currentRoom.getRoomCode())
                        .queryParam("currency", currentRoom.getCurrency()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Room> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        assertThat(result).isEqualTo(expected);
    }

    @Test
//    @Sql({"classpath:/room/create.sql"})
    public void testBestPrice() throws Exception {
        Room bestPrice1 = expected.get(0).withPrice(1.0);
        Room bestPrice2 = expected.get(1).withPrice(1.0);

        List<Room> bestPrice = List.of(bestPrice1, bestPrice2);
        roomRepository.saveAll(bestPrice);

        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/" + BEST_PRICE)
                        .queryParam("roomCode", currentRoom.getRoomCode())
                        .queryParam("currency", currentRoom.getCurrency()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Room> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        assertThat(result).isEqualTo(bestPrice);
    }

    @Test
//    @Sql({"classpath:/hotel/create.sql"})
    public void testFindAllHotels() throws Exception {
//        String jsonResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertThat(jsonResult).isNotNull();
//
//        List<Hotel> hotels = objectMapper.readValue(
//                jsonResult, new TypeReference<>() {
//                });
//
//        // Nothing really to check here other than ensuring
//        // that the airport list is not empty and no entries
//        // have null or blank fields.
//        assertThat(hotels.size()).isGreaterThan(0);
//
//        for (Hotel hotel : hotels) {
//            assertThat(hotel.getStreet()).isNotBlank();
//            assertThat(hotel.getCity()).isNotBlank();
//            assertThat(hotel.getState()).isNotBlank();
//            assertThat(hotel.getZipcode()).isNotBlank();
//            assertThat(hotel.getCountry()).isNotBlank();
//            assertThat(hotel.getHotelName()).isNotBlank();
//        }
        List<Hotel> expected = List.of(
                new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                        "Bal Harbour", "FL", "33154", "USA", rooms),
                new Hotel(2L, "MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW",
                        "Atlanta", "GA", "30305", "USA", rooms),
                new Hotel(3L, "MAR", "JW Marriott", "201 8th Ave S",
                        "Nashville", "TN", "37203", "USA", rooms)
                );

        hotelRepository.saveAll(expected);

        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/" + HOTELS))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(jsonResult).isNotNull();

        List<Hotel> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        assertThat(result).isEqualTo(expected);
    }

    @Test
//    @Sql({"classpath:/room/create.sql"})
    public void testFindAllRooms() throws Exception {
        Hotel hotel = new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                        "Bal Harbour", "FL", "33154", "USA", rooms);

        List<Room> expected = new ArrayList<>();
        Room room1 = Room.builder()
                .guests(2)
                .roomCode("king")
                .price(570.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        expected.add(room1);
        Room room2 = Room.builder()
                .guests(1)
                .roomCode("queen")
                .price(240.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        expected.add(room2);
        Room room3 = Room.builder()
                .guests(3)
                .roomCode("double-double")
                .price(620.0)
                .currency("USD")
                .hotel(hotel)
                .build();
        expected.add(room3);

        roomRepository.saveAll(expected);

        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/" + ROOMS))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(jsonResult).isNotNull();

        List<Room> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        assertThat(result).isEqualTo(expected);
    }

//    @Test
//    @Sql({"classpath:/room/create.sql"})
//    public void testFindCheckInDates() throws Exception {
//        String jsonResult = mockMvc.perform(
//                MockMvcRequestBuilders.get("/" + ROOM_DATES)
//                        .queryParam("hotelId", "MAR"))
//                        //.queryParam("arrivalAirport", "MOB"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        List<LocalDate> expected = new ArrayList<>(2);
//        expected.add(LocalDate.parse("2021-02-24"));
//        expected.add(LocalDate.parse("2021-02-25"));
//
//        List<LocalDate> result = objectMapper.readValue(
//                jsonResult, new TypeReference<>() {
//                });
//
//        // Make sure the controller was to return a non-empty list.
//        assertThat(result).isEqualTo(expected);
//    }
}*/
