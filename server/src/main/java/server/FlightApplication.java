package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * This class provides the entry point into the Spring application
 */
@SpringBootApplication
@EnableJpaRepositories
public class FlightApplication {
    /**
     * A static main() entry point is needed to
     * run this Spring application.
     */
    public static void main(String[] args) {
        // Launch this application.
        SpringApplication.run(FlightApplication.class, args);
    }
}