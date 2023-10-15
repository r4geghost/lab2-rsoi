package ru.dyusov.FlightService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dyusov.FlightService.domain.Flight;
import ru.dyusov.FlightService.response.FlightListResponse;
import ru.dyusov.FlightService.response.FlightResponse;
import ru.dyusov.FlightService.service.FlightService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public FlightListResponse getFlights(@RequestParam int page, @RequestParam int size) {
        List<Flight> flights = flightService.getFlights(page, size);
        List<FlightResponse> flightResponses = new ArrayList<>();
        for (Flight flight : flights) {
            flightResponses.add(FlightResponse.build(
                    flight.getFlightNumber(),
                    String.format("%s %s", flight.getFrom_airport_id().getCity(), flight.getFrom_airport_id().getName()),
                    String.format("%s %s", flight.getTo_airport_id().getCity(), flight.getTo_airport_id().getName()),
                    flight.getDateTime(),
                    flight.getPrice()
            ));
        }
        return FlightListResponse.build(page, size, flights.size(), flightResponses);
    }

    @GetMapping("/flights/{flightNumber}")
    public FlightResponse getFlightById(@PathVariable("flightNumber") String flightNumber) {
        Flight flight = flightService.getFlightByFlightNumber(flightNumber);
        return FlightResponse.build(
                flight.getFlightNumber(),
                String.format("%s %s", flight.getFrom_airport_id().getCity(), flight.getFrom_airport_id().getName()),
                String.format("%s %s", flight.getTo_airport_id().getCity(), flight.getTo_airport_id().getName()),
                flight.getDateTime(),
                flight.getPrice()
        );
    }

    @GetMapping("/manage/health")
    public ResponseEntity<Void> status() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
