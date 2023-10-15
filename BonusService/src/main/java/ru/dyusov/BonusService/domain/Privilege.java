package ru.dyusov.BonusService.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
@Table(name = "privilege")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String username;
    String status;
    int balance;
}
