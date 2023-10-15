package ru.dyusov.TickerService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dyusov.TickerService.domain.Ticket;
import ru.dyusov.TickerService.model.TicketRequest;
import ru.dyusov.TickerService.service.TicketService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/tickets")
    public UUID addTicket(@RequestBody TicketRequest ticket) {
        return ticketService.addTicket(ticket);
    }

    @GetMapping("/tickets")
    public List<Ticket> getTicketsOfUser(@RequestHeader(name = "X-User-Name") String username) {
        return ticketService.getTickets(username);
    }

    @GetMapping("/tickets/{ticketUid}")
    public Ticket getTicketByUid(@PathVariable UUID ticketUid, @RequestHeader(name = "X-User-Name") String username) {
        return ticketService.getTicketByUid(ticketUid, username);
    }

    @PatchMapping("/tickets/{ticketUid}")
    public Ticket updateOperationType(@PathVariable UUID ticketUid, @RequestHeader(name = "X-User-Name") String username, @RequestBody Map<String, Object> fields) {
        return ticketService.updateTicket(username, ticketUid, fields);
    }

    @GetMapping("/manage/health")
    public ResponseEntity<Void> status() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
