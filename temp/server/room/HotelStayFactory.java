/*
package server.room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import org.apache.tomcat.jni.Local;
import server.common.model.Room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

*/
/**
 * This class creates a list random round flight flights for any number
 * of airlines, airports, travel dates, and number of daily flights.
 *//*

public class HotelStayFactory {
    private static final Random random = new Random();
    private static final int ROOMS = 2;
    private static final int HOTELS = 2;
    private static final int ROOMS_PER_HOTEL = 1;
    private static final int DAYS = 3;
    private static final String CURRENCY = "USD";
    private static final int MAX_ROOMS = 10;
    private static final int MAX_HOTELS = 10;
    private static final int BASE_PRICE = 50;


    public static RandomHotelStayBuilder builder() {
        return new RandomHotelStayBuilder();
    }

    public static class RandomHotelStayBuilder {
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(DAYS);
        String currency = CURRENCY;
        List<String> rooms = randomStrings(ROOMS);
        List<String> hotels = randomStrings(HOTELS);
        int basePrice = BASE_PRICE;
        int dailyRoomsAvailable = ROOMS_PER_HOTEL * HOTELS;

        public RandomHotelStayBuilder basePrice(int basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public RandomHotelStayBuilder hotels(int hotels) {
            assertThat(hotels).isLessThan(MAX_HOTELS);
            this.hotels = randomStrings(hotels);
            return this;
        }

        public RandomHotelStayBuilder hotels(String... hotels) {
            hotels(Arrays.asList(hotels));
            return this;
        }

        public RandomHotelStayBuilder hotels(List<String> hotels) {
            assertThat(hotels).hasSizeLessThanOrEqualTo(MAX_HOTELS);
            this.hotels = hotels.stream()
                    .distinct()
                    .collect(Collectors.toList());
            return this;
        }

        public RandomHotelStayBuilder rooms(int rooms) {
            assertThat(rooms).isLessThan(MAX_ROOMS);
            this.rooms = randomStrings(rooms);
            return this;
        }

        public RandomHotelStayBuilder rooms(String... rooms) {
            rooms(Arrays.asList(rooms));
            return this;
        }

        public RandomHotelStayBuilder rooms(List<String> rooms) {
            assertThat(rooms).hasSizeLessThanOrEqualTo(MAX_ROOMS);
            this.rooms = rooms.stream()
                    .distinct()
                    .collect(Collectors.toList());
            return this;
        }

        public RandomHotelStayBuilder dailyRoomsAvailable(int dailyRoomsAvailable) {
            this.dailyRoomsAvailable = dailyRoomsAvailable;
            return this;
        }

        public RandomHotelStayBuilder checkInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
            return this;
        }

        public RandomHotelStayBuilder checkInDate(String checkInDate) {
            this.checkInDate = LocalDate.parse(checkInDate);
            return this;
        }

        public RandomHotelStayBuilder checkOutDate(LocalDate checkOutDate) {
            this.checkOutDate = checkOutDate;
            return this;
        }

        public RandomHotelStayBuilder checkOutDate(String checkOutDate) {
            this.checkOutDate = LocalDate.parse(checkOutDate);
            return this;
        }

        public RandomHotelStayBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public List<Room> build() {
            assertTrue(checkOutDate.equals(checkInDate) || checkOutDate.isAfter(checkInDate));
            assertThat(hotels).isNotEmpty();
            assertThat(rooms).hasSizeGreaterThan(1);
            assertThat(dailyRoomsAvailable).isGreaterThan(0);
            assertTrue(!currency.isBlank() && currency.length() <= 3);

            return randomHotelStay(this);
        }
    }

    private static List<Room> randomHotelStay(RandomHotelStayBuilder builder) {
        //populate table with random room data
        //generate data for hotels that exist, don't create more stays than rooms in hotels (later problem)

//        List<FlightPath> flightPaths = new ArrayList<>();
//        for (int i = 0; i < builder.airports.size() - 1; i++) {
//            String fromAirport = builder.airports.get(i);
//            for (int j = i + 1; j < builder.airports.size(); j++) {
//                String toAirport = builder.airports.get(j);
//                int distance = random(builder.minDistanceKm, builder.maxDistanceKm);
//                flightPaths.add(FlightPath.builder()
//                        .fromAirport(fromAirport)
//                        .toAirport(toAirport)
//                        .distance(distance)
//                        .build());
//                flightPaths.add(FlightPath.builder()
//                        .fromAirport(toAirport)
//                        .toAirport(fromAirport)
//                        .distance(distance)
//                        .build());
//            }
//        }

        List<Room> rooms =
                builder.checkInDate.datesUntil(builder.checkOutDate.plusDays(1)).flatMap(date ->
                        builder.hotels.stream().flatMap(airline ->
                                flightPaths.stream().flatMap(flight ->
                                        IntStream.range(0, builder.dailyRoomsAvailable).mapToObj(__ ->
                                                randomFlight(
                                                        flight.fromAirport,
                                                        flight.toAirport,
                                                        date,
                                                        flight.distance,
                                                        builder.basePrice,
                                                        builder.minPricePerKm,
                                                        builder.maxPricePerKm,
                                                        airline,
                                                        builder.currency)
                                        )
                                )
                        )
                ).collect(Collectors.toList());

        long days = builder.checkInDate.datesUntil(builder.checkOutDate.plusDays(1)).count();
        long expected = builder.hotels.size()
                * flightPaths.size()
                * days
                * builder.dailyRoomsAvailable;
        assertThat(rooms.size()).isEqualTo(expected);

        return rooms;
    }

    private static Room randomHotelStay(
            String hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int basePrice,
            String roomCode,
            String currency
    ) {
        assertThat(checkInDate).isNotEqualTo(checkOutDate);

        //LocalTime flightTime = flightTime(distance);
//        LocalTime departureTime = randomDepartureTime(flightTime);
//        LocalTime arrivalTime = arrivalTime(departureTime, flightTime);

//        assertThat(departureTime.until(arrivalTime, ChronoUnit.MINUTES))
//                .isGreaterThan(RUNWAY_MINUTES);

        return Room.builder()
                .hotelId(hotelId)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .price(basePrice)
                .roomCode(roomCode)
                .currency(currency)
                .build();
    }

    private static double random(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    private static int random(int min, int max) {
        return (int) random((double) min, max);
    }

//    private static int randomPrice(
//            int distance, int basePrice, double min, double max) {
//        return basePrice + (int) (distance * random(min, max));
//    }

//    private static LocalTime flightTime(int distance) {
//        return LocalTime.ofSecondOfDay(RUNWAY_MINUTES +
//                (long) ((double) distance / FLIGHT_SPEED_KPH * 60 * 60));
//    }

//    private static LocalTime randomDepartureTime(LocalTime flightTime) {
//        // Latest departure hour 24 hours - flightTime.hours.
//        return LocalTime.of(
//                random.nextInt(23 - flightTime.getHour()),
//                random.nextInt(60));
//    }
//
//    private static LocalTime arrivalTime(LocalTime departureTime, LocalTime flightTime) {
//        return LocalTime.from(departureTime)
//                .plusHours(flightTime.getHour())
//                .plusMinutes(flightTime.getMinute());
//    }

    private static List<String> randomStrings(int count) {
        List<String> strings = new ArrayList<>();
        int length = (int) (Math.log10(count) + 1);
        for (int i = 1; i <= count; i++) {
            strings.add(String.format("%0" + length + "d", i));
        }

        return strings;
    }

    */
/**
     * For testing.
     *//*

    public static void main(String[] args) {
        List<Room> rooms = builder().hotels("MAR").build();

        rooms.sort(Comparator.comparing(Room::getCheckInDate));
        System.out.println(Room.toSqlInsertString(rooms));
    }
}*/
