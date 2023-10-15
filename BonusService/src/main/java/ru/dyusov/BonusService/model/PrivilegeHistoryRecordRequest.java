package ru.dyusov.BonusService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class PrivilegeHistoryRecordRequest {
    UUID ticketUid;
    int balanceDiff;
    String operationType;
}
