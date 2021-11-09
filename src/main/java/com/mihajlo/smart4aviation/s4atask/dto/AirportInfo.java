package com.mihajlo.smart4aviation.s4atask.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirportInfo {
    private Integer numberOfDepartures;
    private Integer numberOfArrivals;
    private Integer numberOfBaggageArriving;
    private Integer numberOfBaggageDeparting;
}
