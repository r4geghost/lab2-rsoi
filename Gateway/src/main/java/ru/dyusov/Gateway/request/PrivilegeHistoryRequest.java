package ru.dyusov.Gateway.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class PrivilegeHistoryRequest {
    UUID ticketUid;
    int balanceDiff;
    String operationType;
}
