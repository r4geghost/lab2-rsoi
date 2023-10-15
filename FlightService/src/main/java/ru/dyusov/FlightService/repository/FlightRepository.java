package ru.dyusov.FlightService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dyusov.FlightService.domain.Flight;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    Optional<Flight> findByFlightNumber(String flightNumber);
}
