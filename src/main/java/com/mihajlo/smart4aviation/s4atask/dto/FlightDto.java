package com.mihajlo.smart4aviation.s4atask.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class FlightDto {
    private Long flightId;
    private Integer flightNumber;
    private String departureAirportIATACode;
    private String arrivalAirportIATACode;
    private OffsetDateTime departureDate;
}