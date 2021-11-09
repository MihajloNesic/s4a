package com.mihajlo.smart4aviation.s4atask.service.impl;

import com.mihajlo.smart4aviation.s4atask.dto.AirportInfo;
import com.mihajlo.smart4aviation.s4atask.dto.FlightWeightInfo;
import com.mihajlo.smart4aviation.s4atask.entity.Flight;
import com.mihajlo.smart4aviation.s4atask.entity.domain.CargoType;
import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;
import com.mihajlo.smart4aviation.s4atask.exception.FlightException;
import com.mihajlo.smart4aviation.s4atask.repositry.FlightRepository;
import com.mihajlo.smart4aviation.s4atask.service.CargoService;
import com.mihajlo.smart4aviation.s4atask.service.FlightService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;
    private CargoService cargoService;

    @Override
    public List<FlightWeightInfo> getFlightWeightInfo(Integer flightNumber, Date date, WeightUnit weightUnit) {
        List<Flight> flights = flightRepository.findAllByFlightNumberAndDate(flightNumber, date);
        List<FlightWeightInfo> response = flights.stream()
                .map(flight -> {
                    Double cargoWeight = cargoService.calculateFlightCargo(flight.getId(), CargoType.CARGO, weightUnit);
                    Double baggageWeight = cargoService.calculateFlightCargo(flight.getId(), CargoType.BAGGAGE, weightUnit);
                    Double total = Double.sum(cargoWeight, baggageWeight);
                    return FlightWeightInfo.builder()
                            .flightId(flight.getId())
                            .cargo(cargoWeight)
                            .baggage(baggageWeight)
                            .total(total)
                            .build();
                }).collect(Collectors.toList());
        return response;
    }

    @Override
    public AirportInfo getAirportInfo(String iataCode, Date date) {
        List<Flight> flights = flightRepository.findAllByAirportCodeAndDate(iataCode, date);

        if (flights.isEmpty()) {
            throw new FlightException("No arrivals or departures from airport '" + iataCode + "' for date '" + date + "'");
        }

        int numberOfDepartures = 0;
        int numberOfArrivals = 0;
        int numberOfBaggageArriving = 0;
        int numberOfBaggageDeparting = 0;

        for (Flight flight : flights) {
            Integer baggageNumber = cargoService.calculateFlightCargoNumber(flight.getId(), CargoType.BAGGAGE);
            if (flight.getArrivalAirportIATACode().equals(iataCode)) {
                numberOfArrivals++;
                numberOfBaggageArriving += baggageNumber;
            }
            else if (flight.getDepartureAirportIATACode().equals(iataCode)) {
                numberOfDepartures++;
                numberOfBaggageDeparting += baggageNumber;
            }
        }

        return AirportInfo.builder()
                .numberOfDepartures(numberOfDepartures)
                .numberOfArrivals(numberOfArrivals)
                .numberOfBaggageDeparting(numberOfBaggageDeparting)
                .numberOfBaggageArriving(numberOfBaggageArriving)
                .build();
    }
}
