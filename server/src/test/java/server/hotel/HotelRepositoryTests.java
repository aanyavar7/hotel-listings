package server.hotel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import server.common.model.Hotel;
import server.common.model.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {"spring.datasource.data="})
public class HotelRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepository repository;

    @BeforeEach
    public void beforeEach() {
        entityManager.clear();
    }

    @Test
    public void testFindAllHotels() {
        List<Room> rooms = new ArrayList<>();
        Hotel[] expected = new Hotel[]{
                new Hotel(1L, "MAR", "The Ritz Carlton", "10295 Collins Avenue",
                        "Bal Harbour", "FL", "33154", "USA", rooms),
                new Hotel(2L, "MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW",
                        "Atlanta", "GA", "30305", "USA", rooms),
                new Hotel(3L, "MAR", "JW Marriott", "201 8th Ave S",
                        "Nashville", "TN", "37203", "USA", rooms),
//                new Hotel("MAR", "JW Marriott", "3300 Lenox Rd NE", "Atlanta", "GA", "30326", "USA"),
//                new Hotel("HLT", "Hampton Inn by Hilton", "310 4th Ave S", "Nashville", "TN", "37201", "USA"),
//                new Hotel("HLT", "Waldorf Astoria Hotels & Resorts", "3752 Las Vegas Blvd S", "Las Vegas", "NV", "89158", "USA")
        };


        for (Hotel hotel : expected) {
            entityManager.persist(hotel);
        }

        entityManager.flush();

        List<Hotel> results = repository.findAll();

        assertEquals(Arrays.asList(expected), results);
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


        for (Hotel hotel : expected) {
            entityManager.persist(hotel);
        }

        entityManager.flush();

        List<Hotel> results = repository.findByCityAndStateAndCountry("Atlanta", "GA", "USA");

        assertEquals(List.of(expected.get(1)), results);
    }
}