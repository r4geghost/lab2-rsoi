package ru.dyusov.TickerService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dyusov.TickerService.domain.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findAllByUsername(String username);

    Optional<Ticket> findByTicketUidAndUsername(UUID ticketUid, String username);

}
