package server.flight;

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

import server.common.model.Airport;
import server.common.model.Flight;
import server.common.model.FlightRequest;

import static server.common.Constants.EndPoint.FLIGHTS;
import static server.common.Constants.EndPoint.FLIGHT_DATES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
@AutoConfigureMockMvc
public class FlightIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"classpath:/airline/create.sql"})
    public void testFindFlights() throws Exception {
        List<Flight> expected = new ArrayList<>(2);
        LocalDate date = LocalDate.parse("2021-02-24");
        String departureAirport = "BTV";
        String arrivalAirport = "MOB";
        String currency = "USD";

        expected.add(
                Flight.builder()
                        .departureAirport(departureAirport)
                        .departureDate(date)
                        .departureTime(LocalTime.parse("11:44:00"))
                        .arrivalAirport(arrivalAirport)
                        .arrivalDate(date)
                        .arrivalTime(LocalTime.parse("17:51:00"))
                        .price(872.92)
                        .currency(currency)
                        .kilometers(1211)
                        .airlineCode("SWA")
                        .build()
        );
        expected.add(
                Flight.builder()
                        .departureAirport(departureAirport)
                        .departureDate(date)
                        .departureTime(LocalTime.parse("10:11:00"))
                        .arrivalAirport(arrivalAirport)
                        .arrivalDate(date)
                        .arrivalTime(LocalTime.parse("12:38:00"))
                        .price(447.87)
                        .currency(currency)
                        .kilometers(721)
                        .airlineCode("AA")
                        .build()
        );

        FlightRequest flightRequest = FlightRequest.builder()
                .departureAirport(departureAirport)
                .departureDate(date)
                .arrivalAirport(arrivalAirport)
                .arrivalDate(date)
                .currency(currency)
                .build();

        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Flight> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        assertThat(result).hasSameSizeAs(expected);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).withId(null)).isEqualTo(expected.get(i));
        }
    }

    @Test
    @Sql({"classpath:/airline/create.sql"})
    public void testBestPrice() throws Exception {
        LocalDate date = LocalDate.parse("2021-02-24");
        String departureAirport = "BTV";
        String arrivalAirport = "MOB";
        String currency = "USD";

        Flight expected = Flight.builder()
                .departureAirport(departureAirport)
                .departureDate(date)
                .departureTime(LocalTime.parse("10:11:00"))
                .arrivalAirport(arrivalAirport)
                .arrivalDate(date)
                .arrivalTime(LocalTime.parse("12:38:00"))
                .price(447.87)
                .currency(currency)
                .kilometers(721)
                .airlineCode("AA")
                .build();

        FlightRequest flightRequest = FlightRequest.builder()
                .departureAirport(departureAirport)
                .departureDate(date)
                .arrivalAirport(arrivalAirport)
                .arrivalDate(date)
                .currency(currency)
                .build();

        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/best-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Flight result = objectMapper.readValue(jsonResult, Flight.class);

        assertThat(result.withId(null)).isEqualTo(expected);
    }

    @Test
    @Sql({"classpath:/airport/create.sql"})
    public void testFindAllAirports() throws Exception {
        String jsonResult = mockMvc.perform(MockMvcRequestBuilders.get("/airports"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(jsonResult).isNotNull();

        List<Airport> airports = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        // Nothing really to check here other than ensuring
        // that the airport list is not empty and no entries
        // have null or blank fields.
        assertThat(airports.size()).isGreaterThan(0);

        for (Airport airport : airports) {
            assertThat(airport.getAirportCode()).isNotBlank();
            assertThat(airport.getAirportName()).isNotBlank();
        }
    }

    @Test
    @Sql({"classpath:/airline/create.sql"})
    public void testFindAllFlights() throws Exception {
        String jsonResult = mockMvc.perform(MockMvcRequestBuilders.get("/" + FLIGHTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Flight> result = objectMapper.readValue(
                jsonResult, new TypeReference<>() {
                });

        // Make sure the controller was to return a non-empty list.
        assertThat(result.size()).isGreaterThan(0);
    }

    @Test
    @Sql({"classpath:/airline/create.sql"})
    public void testFindFlightDates() throws Exception {
        String jsonResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/" + FLIGHT_DATES)
                        .queryParam("departureAirport", "BTV")
                        .queryParam("arrivalAirport", "MOB"))
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