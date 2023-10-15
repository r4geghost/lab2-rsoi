package ru.dyusov.TickerService.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_uid")
    UUID ticketUid;
    String username;
    @Column(name = "flight_number")
    String flightNumber;
    int price;
    String status;
}
