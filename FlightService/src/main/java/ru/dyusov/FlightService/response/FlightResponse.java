package ru.dyusov.FlightService.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class FlightResponse {
    String flightNumber;
    String fromAirport;
    String toAirport;
    String date;
    int price;
}
