package server.vacation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import server.common.model.Hotel;
import server.common.model.Room;
import server.common.model.RoomRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static server.common.Constants.EndPoint.*;

@SpringBootTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
@AutoConfigureMockMvc
public class RoomIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"classpath:/room/create.sql"})
    public void testFindRooms() throws Exception {
        List<Room> expected = new ArrayList<>(2);
        LocalDate date = LocalDate.parse("2021-02-24");
        LocalDate date2 = date.plusDays(5);
        String hotelId = "HLT";
        String currency = "USD";

        expected.add(
                Room.builder()
                        .hotelId(hotelId)
                        .checkInDate(date)
                        .checkOutDate(date2)
                        .guests(2)
                        .roomCode("queen")
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
                        .roomCode("queen")
                        .price(872.00)
                        .currency(currency)
                        .build()
        );

        RoomRequest roomRequest = RoomRequest.builder()
                    .checkInDate(date)
                    .checkOutDate(date2)
                    .hotelId("MAR")
                    .guests(2)
                    .roomCode("king")
                    .build();

        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/hotelStays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Room> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        assertThat(result).hasSameSizeAs(expected);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).withId(null)).isEqualTo(expected.get(i));
        }
    }

    @Test
    @Sql({"classpath:/room/create.sql"})
    public void testBestPrice() throws Exception {
        LocalDate date = LocalDate.parse("2021-02-24");
        LocalDate date2 = date.plusDays(5);
        String hotelId = "ATL";
        String currency = "USD";

        Room expected = Room.builder()
                .hotelId(hotelId)
                .checkInDate(date)
                .checkOutDate(date2)
                .guests(2)
                .roomCode("king")
                .price(532.74)
                .currency(currency)
                .build();

        RoomRequest roomRequest = RoomRequest.builder()
                .checkInDate(date)
                .checkOutDate(date2)
                .hotelId("ATL")
                .guests(2)
                .roomCode("queen")
                .build();

        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/best-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Room result = objectMapper.readValue(jsonResult, Room.class);

        assertThat(result.withId(null)).isEqualTo(expected);
    }

    @Test
    @Sql({"classpath:/server.hotel/create.sql"})
    public void testFindAllHotels() throws Exception {
        String jsonResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(jsonResult).isNotNull();

        List<Hotel> hotels = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        // Nothing really to check here other than ensuring
        // that the airport list is not empty and no entries
        // have null or blank fields.
        assertThat(hotels.size()).isGreaterThan(0);

        for (Hotel hotel : hotels) {
            assertThat(hotel.getStreet()).isNotBlank();
            assertThat(hotel.getCity()).isNotBlank();
            assertThat(hotel.getState()).isNotBlank();
            assertThat(hotel.getZipcode()).isNotBlank();
            assertThat(hotel.getCountry()).isNotBlank();
            assertThat(hotel.getHotelName()).isNotBlank();
        }
    }

    @Test
    @Sql({"classpath:/room/create.sql"})
    public void testFindAllRooms() throws Exception {
        String jsonResult = mockMvc.perform(MockMvcRequestBuilders.get("/" + HOTELS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Room> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        // Make sure the controller was to return a non-empty list.
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    @Sql({"classpath:/room/create.sql"})
    public void testFindCheckInDates() throws Exception {
        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/" + ROOM_DATES)
                        .queryParam("hotelId", "MAR"))
                        //.queryParam("arrivalAirport", "MOB"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<LocalDate> expected = new ArrayList<>(2);
        expected.add(LocalDate.parse("2021-02-24"));
        expected.add(LocalDate.parse("2021-02-25"));

        List<LocalDate> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        // Make sure the controller was to return a non-empty list.
        assertThat(result).isEqualTo(expected);
    }
}