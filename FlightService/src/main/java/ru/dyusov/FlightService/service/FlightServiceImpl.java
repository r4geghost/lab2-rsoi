package ru.dyusov.FlightService.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.dyusov.FlightService.domain.Flight;
import ru.dyusov.FlightService.repository.FlightRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public Flight getFlightByFlightNumber(String flightNumber) {
        Optional<Flight> flight = flightRepository.findByFlightNumber(flightNumber);
        if (flight.isPresent()) {
            return flight.get();
        } else {
            throw new EntityNotFoundException("Flight with flightNumber=" + flightNumber + " not found");
        }
    }

    @Override
    public List<Flight> getFlights(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Flight> flights = flightRepository.findAll(pageable);
        return flights.getContent();
    }
}
