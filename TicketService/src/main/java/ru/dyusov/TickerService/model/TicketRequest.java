package ru.dyusov.TickerService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class TicketRequest {
    String username;
    String flight_number;
    int price;
    String status;
}
