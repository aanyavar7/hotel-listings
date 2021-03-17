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
        public static final String AIRPORT = "airport";
        public static final String EXCHANGE = "exchange";
        public static final String FLIGHT = "flight";
        public static final String AIRLINE_AA = "aa";
        public static final String AIRLINE_SWA = "swa";
    }

    /**
     * Service end-points.
     */
    public static class EndPoint {
        public static final String RATE = "rate";
        public static final String AIRPORTS = "airports";
        public static final String BEST_PRICE = "best-price";
        public static final String FLIGHTS = "flights";
        public static final String FLIGHT_DATES = "dates";
    }
}