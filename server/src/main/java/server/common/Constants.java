package server.common;

/**
 * Static class used to centralize all constants used by
 * this application.
 */
public class Constants {
    /**
     * Service names.
     */
    public static class Service {
        public static final String HOTEL = "hotel";
        public static final String EXCHANGE = "exchange";
        public static final String ROOM = "room";
        public static final String HOTEL_MAR = "mar";
        public static final String HOTEL_HlT = "hlt";
    }

    /**
     * Service end-points.
     */
    public static class EndPoint {
        public static final String RATE = "rate";
        public static final String HOTELS = "hotels";
        public static final String BEST_PRICE = "best-price";
        public static final String ROOMS = "rooms";
        public static final String HOTEL_LOCATIONS = "locations";
        public static final String ROOM_CODES = "room codes";
    }
}