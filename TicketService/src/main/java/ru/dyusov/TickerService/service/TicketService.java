package ru.dyusov.TickerService.service;

import org.springframework.stereotype.Service;
import ru.dyusov.TickerService.domain.Ticket;
import ru.dyusov.TickerService.model.TicketRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface TicketService {
    public UUID addTicket(TicketRequest request);

    public Ticket getTicketByUid(UUID ticketUid, String username);

    public List<Ticket> getTickets(String username);

    Ticket updateTicket(String username, UUID ticketUid, Map<String, Object> fields);
}
