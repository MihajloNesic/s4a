package com.mihajlo.smart4aviation.s4atask.entity;

import com.mihajlo.smart4aviation.s4atask.dto.FlightDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer flightNumber;
    @Column(name = "departure_airport_iata_code")
    private String departureAirportIATACode;
    @Column(name = "arrival_airport_iata_code")
    private String arrivalAirportIATACode;
    private LocalDateTime departureDate;
    private String zoneOffset;

    public static Flight createFromDto(FlightDto flightDto) {
        return Flight.builder()
                .id(flightDto.getFlightId())
                .flightNumber(flightDto.getFlightNumber())
                .departureAirportIATACode(flightDto.getDepartureAirportIATACode())
                .arrivalAirportIATACode(flightDto.getArrivalAirportIATACode())
                .departureDate(flightDto.getDepartureDate().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
                .zoneOffset(flightDto.getDepartureDate().getOffset().toString())
                .build();
    }

    public ZoneOffset getZoneOffset() {
        return ZoneOffset.of(this.zoneOffset);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber=" + flightNumber +
                ", departureAirportIATACode='" + departureAirportIATACode + '\'' +
                ", arrivalAirportIATACode='" + arrivalAirportIATACode + '\'' +
                ", departureDate=" + departureDate +
                ", zoneOffset='" + getZoneOffset() + '\'' +
                '}';
    }
}
