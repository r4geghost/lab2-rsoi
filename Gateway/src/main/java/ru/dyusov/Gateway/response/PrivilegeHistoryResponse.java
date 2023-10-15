package ru.dyusov.Gateway.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class PrivilegeHistoryResponse {
    String date;
    UUID ticketUid;
    int balanceDiff;
    String operationType;
}
