package server.airport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import server.common.model.Airport;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Ensure that app does not create and populate repositories.
@DataJpaTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
public class AirportRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirportRepository repository;

    @BeforeEach
    public void beforeEach() {
        entityManager.clear();
    }

    @Test
    public void testFindAllAirports() {
        Airport[] expected = new Airport[]{
                new Airport("CAK", "Akron/Canton, OH"),
                new Airport("ALB", "Albany, NY"),
                new Airport("ABQ", "Albuquerque, NM"),
                new Airport("ABE", "Bethlehem, PA"),
                new Airport("AMA", "Amarillo, TX"),
                new Airport("ANC", "Anchorage, AK")
        };


        for (Airport airport : expected) {
            entityManager.persist(airport);
        }

        entityManager.flush();

        List<Airport> results = repository.findAll();

        assertEquals(Arrays.asList(expected), results);
    }
}