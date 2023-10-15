package ru.dyusov.FlightService.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String flightNumber;
    String dateTime;
    @ManyToOne
    @JoinColumn(name = "from_airport_id", referencedColumnName = "id")
    Airport from_airport_id;
    @ManyToOne
    @JoinColumn(name = "to_airport_id", referencedColumnName = "id")
    Airport to_airport_id;
    int price;
}
