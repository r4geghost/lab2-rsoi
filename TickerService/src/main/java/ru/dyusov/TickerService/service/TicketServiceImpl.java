package ru.dyusov.TickerService.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.dyusov.TickerService.domain.Ticket;
import ru.dyusov.TickerService.model.TicketRequest;
import ru.dyusov.TickerService.repository.TicketRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public UUID addTicket(TicketRequest request) {
        Ticket saved = Ticket.build(
                0,
                UUID.randomUUID(),
                request.getUsername(),
                request.getFlight_number(),
                request.getPrice(),
                request.getStatus()
        );
        ticketRepository.save(saved);
        return saved.getTicketUid();
    }

    @Override
    public Ticket getTicketByUid(UUID ticketUid, String username) {
        Optional<Ticket> ticket = ticketRepository.findByTicketUidAndUsername(ticketUid, username);
        if (ticket.isPresent()) {
            return ticket.get();
        } else {
            throw new EntityNotFoundException("Ticket with uid=" + ticketUid + " not found for user " + username);
        }
    }

    @Override
    public List<Ticket> getTickets(String username) {
        return ticketRepository.findAllByUsername(username);
    }

    @Override
    public Ticket updateTicket(String username, UUID ticketUid, Map<String, Object> fields) {
        Optional<Ticket> ticket = ticketRepository.findByTicketUidAndUsername(ticketUid, username);
        if (ticket.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Ticket.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, ticket.get(), value);
            });
            return ticketRepository.save(ticket.get());
        }
        return null;
    }
}
