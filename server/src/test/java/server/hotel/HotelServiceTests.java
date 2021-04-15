package server.hotel;

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
public class HotelServiceTests {
    @MockBean
    HotelRepository hotelRepository;

    @Autowired
    HotelService hotelService;

    @Test
    public void testFindAllHotels() {
        List<Room> rooms = new ArrayList<>();
        List<Hotel> expected = List.of(
                new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                        "Bal Harbour", "FL", "33154", "USA", rooms),
                new Hotel(2L, "MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW",
                        "Atlanta", "GA", "30305", "USA", rooms),
                new Hotel(3L, "MAR", "JW Marriott", "201 8th Ave S",
                        "Nashville", "TN", "37203", "USA", rooms)
        );

        when(hotelRepository.findAll()).thenReturn(expected);

        List<Hotel> result = hotelService.findHotels();

        assertThat(result).isEqualTo(expected);
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    public void testFindHotelsByCityAndStateAndCountry() {
        List<Room> rooms = new ArrayList<>();
        List<Hotel> expected = List.of(
                new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                        "Bal Harbour", "FL", "33154", "USA", rooms),
                new Hotel(2L, "MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW",
                        "Atlanta", "GA", "30305", "USA", rooms),
                new Hotel(3L, "MAR", "JW Marriott", "201 8th Ave S",
                        "Nashville", "TN", "37203", "USA", rooms)
        );

        String city = "city";
        String state = "state";
        String country = "country";

        when(hotelRepository.findByCityAndStateAndCountry(city, state, country))
                .thenReturn(expected);

        assertThat(hotelService.findHotelsByLocation(city, state, country))
                .isEqualTo(expected);

        verify(hotelRepository, times(1))
                .findByCityAndStateAndCountry(city, state, country);
    }
}
