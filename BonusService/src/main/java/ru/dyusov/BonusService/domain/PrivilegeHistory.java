package ru.dyusov.BonusService.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
@Table(name = "privilege_history")
public class PrivilegeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    Privilege privilege;
    @Column(name = "ticket_uid")
    UUID ticketUid;
    @Column(name = "datetime")
    String date;
    @Column(name = "balance_diff")
    int balanceDiff;
    @Column(name = "operation_type")
    String operationType;
}
