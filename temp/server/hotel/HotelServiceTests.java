package server.hotel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import server.common.model.Hotel;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Ensure that app does not create and populate repositories.
@SpringBootTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
@AutoConfigureMockMvc
// Create test table along with test data.
@Sql({"classpath:/server.hotel/create.sql"})
public class HotelServiceTests {

    @Autowired
    HotelService hotelService;

    @Test
    public void testFindAllHotels() {
        List<Hotel> result = hotelService.findHotels();

        // Nothing really to check here other than ensuring
        // that the hotel list is not empty and no entries
        // have null or blank fields.
        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThan(0);

        for (Hotel hotel : result) {
            assertThat(hotel.getStreet()).isNotBlank();
            assertThat(hotel.getCity()).isNotBlank();
            assertThat(hotel.getState()).isNotBlank();
            assertThat(hotel.getZipcode()).isNotBlank();
            assertThat(hotel.getCountry()).isNotBlank();
            assertThat(hotel.getHotelName()).isNotBlank();
        }
    }

    @Test
    public void testFindHotelsByCityAndStateAndCountry(){

    }
}