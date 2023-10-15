package ru.dyusov.Gateway.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class TicketRequest {
    String flightNumber;
    int price;
    boolean paidFromBalance;
}
