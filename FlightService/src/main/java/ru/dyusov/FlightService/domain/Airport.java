package ru.dyusov.FlightService.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
@Table(name = "airport")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String city;
    String country;
}
