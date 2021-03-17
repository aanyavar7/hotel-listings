package server.airport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import server.common.model.Airport;

import static org.assertj.core.api.Assertions.assertThat;

// Ensure that app does not create and populate repositories.
@SpringBootTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
@AutoConfigureMockMvc
// Create test table along with test data.
@Sql({"classpath:/airport/create.sql"})
public class AirportServiceTests {

    @Autowired
    AirportService airportService;

    @Test
    public void testFindAllAirports() {
        List<Airport> result = airportService.findAirports();

        // Nothing really to check here other than ensuring
        // that the airport list is not empty and no entries
        // have null or blank fields.
        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThan(0);

        for (Airport airport : result) {
            assertThat(airport.getAirportCode()).isNotBlank();
            assertThat(airport.getAirportName()).isNotBlank();
        }
    }
}