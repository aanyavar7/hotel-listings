package server.airport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import server.common.model.Airport;

/**
 * Airport service.
 */
@Service
public class AirportService {
    @Autowired
    private AirportRepository repository;

    public List<Airport> findAirports() {
        return repository.findAll();
    }
}