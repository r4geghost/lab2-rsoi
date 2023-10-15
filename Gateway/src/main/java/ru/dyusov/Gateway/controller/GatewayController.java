package ru.dyusov.Gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.dyusov.Gateway.request.AddTicketRequest;
import ru.dyusov.Gateway.request.PrivilegeHistoryRequest;
import ru.dyusov.Gateway.request.TicketRequest;
import ru.dyusov.Gateway.response.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@PropertySource("classpath:application.properties")
public class GatewayController {

    @Value("${flight_service.host}")
    private String FLIGHT_SERVICE;

    @Value("${ticket_service.host}")
    private String TICKET_SERVICE;

    @Value("${bonus_service.host}")
    private String BONUS_SERVICE;

    private static String GET_FLIGHTS_URL = "/api/v1/flights?page={page}&size={size}";
    private static String GET_FLIGHT_BY_NUMBER_URL = "/api/v1/flights/{flightNumber}";
    private static String GET_TICKETS_URL = "/api/v1/tickets";
    private static String GET_TICKET_BY_UID = "/api/v1/tickets/{ticketUid}";
    private static String GET_PRIVILEGE_URL = "/api/v1/privilege";
    private static String GET_PRIVILEGE_HISTORY_URL = "/api/v1/privilege/history";
    private static String GET_PRIVILEGE_HISTORY_BY_TICKET_UID_URL = "/api/v1/privilege/history/{ticketUid}";

    @PostMapping("/tickets")
    public TicketPurchaseResponse addTicket(@RequestHeader(name = "X-User-Name") String username, @RequestBody TicketRequest ticket) throws Exception {
        try {
            // get flight by flightNumber and check if exist
            FlightResponse flight = new RestTemplate().getForObject(
                    FLIGHT_SERVICE + GET_FLIGHT_BY_NUMBER_URL,
                    FlightResponse.class,
                    ticket.getFlightNumber());

            // add ticket to Tickets of user
            AddTicketRequest request = AddTicketRequest.build(
                    username,
                    ticket.getFlightNumber(),
                    ticket.getPrice(),
                    "PAID"
            );
            ResponseEntity<UUID> addedTicket = new RestTemplate().postForEntity(
                    TICKET_SERVICE + GET_TICKETS_URL,
                    request,
                    UUID.class
            );

            // get ticketUid of added ticket
            UUID ticketUid = addedTicket.getBody();

            try {
                PrivilegeResponse privilege = getPrivilegeInfo(username);
                int bonusBalance = privilege.getBalance();

                int paidByMoney = ticket.getPrice();
                int paidByBonuses = 0;

                if (ticket.isPaidFromBalance()) {
                    // debit all bonuses
                    addHistoryRecord(ticketUid, bonusBalance, "DEBIT_THE_ACCOUNT", username);
                    updateBalance(0, username);
                    paidByBonuses = bonusBalance;
                    paidByMoney = paidByMoney - bonusBalance;
                } else {
                    // add bonus = 10% of ticket price
                    addHistoryRecord(ticketUid, ticket.getPrice() / 10, "FILL_IN_BALANCE", username);
                    updateBalance(bonusBalance + (ticket.getPrice() / 10), username);
                }

                // create response
                return TicketPurchaseResponse.build(
                        ticketUid,
                        flight.getFlightNumber(),
                        flight.getFromAirport(),
                        flight.getToAirport(),
                        flight.getDate(),
                        flight.getPrice(),
                        paidByMoney,
                        paidByBonuses,
                        "PAID",
                        privilege
                );
            } catch (HttpClientErrorException e) {
                throw new Exception("Privilege of user " + username + " not found");
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(404)) {
                throw new Exception("Flight with flightNumber=" + ticket.getFlightNumber() + " not found");
            } else {
                throw new Exception("Unknown error" + e.getMessage());
            }
        }
    }

    @GetMapping("/flights")
    public FlightListResponse getFlights(@RequestParam int page, @RequestParam int size) {
        return new RestTemplate().getForObject(FLIGHT_SERVICE + GET_FLIGHTS_URL, FlightListResponse.class, page - 1, size);
    }

    @GetMapping("/tickets")
    public TicketResponse[] getTickets(@RequestHeader(name = "X-User-Name") String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);
        TicketResponse[] tickets = new RestTemplate().exchange(
                TICKET_SERVICE + GET_TICKETS_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TicketResponse[].class).getBody();
        for (TicketResponse ticket : tickets) {
            FlightResponse flight = new RestTemplate().getForObject(
                    FLIGHT_SERVICE + GET_FLIGHT_BY_NUMBER_URL,
                    FlightResponse.class,
                    ticket.getFlightNumber());
            ticket.setDate(flight.getDate());
            ticket.setFromAirport(flight.getFromAirport());
            ticket.setToAirport(flight.getToAirport());
        }
        return tickets;
    }

    @GetMapping("/me")
    public InfoResponse getUserInfo(@RequestHeader(name = "X-User-Name") String username) throws Exception {
        try {
            TicketResponse[] tickets = getTickets(username);
            PrivilegeResponse privilege = getPrivilegeInfo(username);
            return InfoResponse.build(tickets, privilege);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(404)) {
                throw new Exception("Ticket with of user " + username + " not found");
            } else {
                throw new Exception("Unknown error" + e.getMessage());
            }
        }
    }

    @GetMapping("/tickets/{ticketUid}")
    public TicketResponse getTicketByUid(@PathVariable UUID ticketUid, @RequestHeader(name = "X-User-Name") String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);
        ResponseEntity<TicketResponse> ticketResponse = new RestTemplate().exchange(
                TICKET_SERVICE + GET_TICKET_BY_UID,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TicketResponse.class,
                ticketUid,
                username
        );
        if (ticketResponse.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404));
        } else {
            TicketResponse ticket = ticketResponse.getBody();
            FlightResponse flight = new RestTemplate().getForObject(
                    FLIGHT_SERVICE + GET_FLIGHT_BY_NUMBER_URL,
                    FlightResponse.class,
                    ticket.getFlightNumber());
            ticket.setDate(flight.getDate());
            ticket.setFromAirport(flight.getFromAirport());
            ticket.setToAirport(flight.getToAirport());
            return ticket;
        }
    }

    @DeleteMapping("/tickets/{ticketUid}")
    public void deleteTicketByUid(@PathVariable UUID ticketUid, @RequestHeader(name = "X-User-Name") String username) throws Exception {
        try {
            // get ticket to be deleted
            TicketResponse ticket = getTicketByUid(ticketUid, username);

            try {
                // find privilege status
                PrivilegeResponse privilege = getPrivilegeInfo(username);
                int currentBalance = privilege.getBalance();

                HttpHeaders headers = new HttpHeaders();
                headers.set("X-User-Name", username);

                // find history record of this ticketUid
                PrivilegeHistoryResponse privilegeHistoryResponse =
                        new RestTemplate().exchange(
                                BONUS_SERVICE + GET_PRIVILEGE_HISTORY_BY_TICKET_UID_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers),
                                PrivilegeHistoryResponse.class,
                                ticketUid
                        ).getBody();
                String operationType = privilegeHistoryResponse.getOperationType();
                int balanceDiff = privilegeHistoryResponse.getBalanceDiff();
                int newBalance;
                // refund bonuses
                if (operationType.equals("FILL_IN_BALANCE")) {
                    newBalance = currentBalance - balanceDiff;
                    operationType = "DEBIT_THE_ACCOUNT";
                } else if (operationType.equals("DEBIT_THE_ACCOUNT")) {
                    newBalance = currentBalance + balanceDiff;
                    operationType = "FILL_IN_BALANCE";
                } else {
                    throw new Exception("Unknown type of operation");
                }
                // add history record
                addHistoryRecord(ticket.getTicketUid(), balanceDiff, operationType, username);

                // change bonus balance of user
                Map<String, Object> balanceUpdate = new HashMap<>();
                balanceUpdate.put("balance", newBalance);
                RestTemplate rest = new RestTemplate();
                rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                ResponseEntity<PrivilegeResponse> updatedPrivilege = rest.exchange(
                        BONUS_SERVICE + GET_PRIVILEGE_URL,
                        HttpMethod.PATCH,
                        new HttpEntity<>(headers),
                        PrivilegeResponse.class
                );
            } catch (HttpClientErrorException e) {
                throw new Exception("Privilege of user " + username + " not found");
            }

            // change status to "CANCELED"
            HttpHeaders ticketHeaders = new HttpHeaders();
            ticketHeaders.set("X-User-Name", username);
            Map<String, Object> fields = new HashMap<>();
            fields.put("status", "CANCELED");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            ResponseEntity<TicketResponse> ticketResponseEntity = restTemplate.exchange(
                    TICKET_SERVICE + GET_TICKET_BY_UID,
                    HttpMethod.PATCH,
                    new HttpEntity<>(fields, ticketHeaders),
                    TicketResponse.class,
                    ticketUid
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(404)) {
                throw new Exception("Ticket with of user " + username + " not found");
            } else {
                throw new Exception("Unknown error" + e.getMessage());
            }
        }
    }

    @GetMapping("/privilege")
    public PrivilegeWithHistoryResponse getPrivilegeWithHistory(@RequestHeader(name = "X-User-Name") String username) throws Exception {
        try {
            PrivilegeResponse privilege = getPrivilegeInfo(username);
            List<PrivilegeHistoryResponse> historyList = List.of(getPrivilegeHistory(username));
            return PrivilegeWithHistoryResponse.build(privilege.getBalance(), privilege.getStatus(), historyList);
        } catch (HttpClientErrorException e) {
            throw new Exception("Privilege of user " + username + " not found");
        }
    }

    private PrivilegeHistoryResponse[] getPrivilegeHistory(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);
        return new RestTemplate().exchange(
                BONUS_SERVICE + GET_PRIVILEGE_HISTORY_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PrivilegeHistoryResponse[].class
        ).getBody();
    }

    private PrivilegeResponse getPrivilegeInfo(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);
        ResponseEntity<PrivilegeResponse> privilege = new RestTemplate().exchange(
                BONUS_SERVICE + GET_PRIVILEGE_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PrivilegeResponse.class
        );
        if (privilege.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(404));
        } else {
            return privilege.getBody();
        }
    }

    private String addHistoryRecord(UUID ticketUid, int bonusAmount, String operationType, String username) {
        PrivilegeHistoryRequest request = PrivilegeHistoryRequest.build(
                ticketUid,
                bonusAmount,
                operationType
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);
        HttpEntity<PrivilegeHistoryRequest> historyRecord = new HttpEntity<>(request, headers);
        ResponseEntity<Void> historyResponseEntity = new RestTemplate().postForEntity(
                BONUS_SERVICE + GET_PRIVILEGE_HISTORY_URL,
                historyRecord,
                Void.class
        );
        return historyResponseEntity.getHeaders().get("Location").toString();
    }

    private void updateBalance(int balance, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);
        Map<String, Object> fields = new HashMap<>();
        fields.put("balance", balance);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<PrivilegeResponse> bonusResponseEntity = restTemplate.exchange(
                BONUS_SERVICE + GET_PRIVILEGE_URL,
                HttpMethod.PATCH,
                new HttpEntity<>(fields, headers),
                PrivilegeResponse.class
        );
    }
}
