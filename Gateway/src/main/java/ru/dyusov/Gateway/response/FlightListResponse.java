package ru.dyusov.Gateway.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class FlightListResponse {
    int page;
    int pageSize;
    int totalElements;
    List<FlightResponse> items;
}
