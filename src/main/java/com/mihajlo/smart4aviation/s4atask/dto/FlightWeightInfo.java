package com.mihajlo.smart4aviation.s4atask.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightWeightInfo {
    private Long flightId;
    private Double cargo;
    private Double baggage;
    private Double total;
}
