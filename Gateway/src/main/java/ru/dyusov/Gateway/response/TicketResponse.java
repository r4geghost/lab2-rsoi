package ru.dyusov.Gateway.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class TicketResponse {
    UUID ticketUid;
    String flightNumber;
    String fromAirport;
    String toAirport;
    String date;
    int price;
    String status;
}
