package ru.dyusov.FlightService.service;

import org.springframework.stereotype.Service;
import ru.dyusov.FlightService.domain.Flight;

import java.util.List;

@Service
public interface FlightService {

    public Flight getFlightByFlightNumber(String flightNumber);

    public List<Flight> getFlights(int page, int size);
}
