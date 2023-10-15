package ru.dyusov.Gateway.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class PrivilegeResponse {
    int balance;
    String status;
}
