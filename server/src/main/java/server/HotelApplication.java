package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * This class provides the entry point into the Spring application
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableWebSecurity
public class HotelApplication {
    /**
     * A static main() entry point is needed to
     * run this Spring application.
     */
    public static void main(String[] args) {
        // Launch this application.
        SpringApplication.run(HotelApplication.class, args);
    }
}