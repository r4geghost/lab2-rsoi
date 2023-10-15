package ru.dyusov.Gateway.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class AddTicketRequest {
    String username;
    String flight_number;
    int price;
    String status;
}
