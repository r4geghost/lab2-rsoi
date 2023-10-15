package ru.dyusov.Gateway.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class TicketPurchaseResponse {
    UUID ticketUid;
    String flightNumber;
    String fromAirport;
    String toAirport;
    String date;
    int price;
    int paidByMoney;
    int paidByBonuses;
    String status;
    PrivilegeResponse privilege;
}
