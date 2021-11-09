package com.mihajlo.smart4aviation.s4atask;

import com.mihajlo.smart4aviation.s4atask.dto.AirportInfo;
import com.mihajlo.smart4aviation.s4atask.dto.FlightWeightInfo;
import com.mihajlo.smart4aviation.s4atask.entity.CargoItem;
import com.mihajlo.smart4aviation.s4atask.entity.Flight;
import com.mihajlo.smart4aviation.s4atask.entity.domain.CargoType;
import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;
import com.mihajlo.smart4aviation.s4atask.exception.FlightException;
import com.mihajlo.smart4aviation.s4atask.repositry.CargoItemRepository;
import com.mihajlo.smart4aviation.s4atask.repositry.FlightRepository;
import com.mihajlo.smart4aviation.s4atask.service.CargoService;
import com.mihajlo.smart4aviation.s4atask.service.FlightService;
import com.mihajlo.smart4aviation.s4atask.service.impl.CargoServiceImpl;
import com.mihajlo.smart4aviation.s4atask.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private CargoItemRepository cargoItemRepository;

    @InjectMocks
    private CargoService cargoService = new CargoServiceImpl(cargoItemRepository);
    @InjectMocks
    private FlightService flightService = new FlightServiceImpl(flightRepository, cargoService);

    @Test
    public void whenNonExistingFlightNumberIsPassed_thenEmptyListIsReturned() {
        // parameters
        Integer flightNumber = 1111;
        Date date = new Date(2021, Calendar.NOVEMBER, 9);
        WeightUnit weightUnit = WeightUnit.KILOGRAM;

        // prepare
        when(flightRepository.findAllByFlightNumberAndDate(flightNumber, date))
                .thenReturn(new ArrayList<>());

        // execute
        List<FlightWeightInfo> flightWeightInfoList = flightService.getFlightWeightInfo(flightNumber, date, weightUnit);

        assertEquals(0, flightWeightInfoList.size());
    }

    @Test
    public void whenExistingFlightNumberAndDateArePassed_thenNonEmptyListIsReturned() {
        // parameters
        Integer flightNumber = 1234;
        Date date = new Date(2021, Calendar.NOVEMBER, 9);
        WeightUnit weightUnit = WeightUnit.KILOGRAM;

        // data
        Flight f1 = Flight.builder()
                .id(997L)
                .flightNumber(flightNumber)
                .departureAirportIATACode("AAA")
                .arrivalAirportIATACode("BBB")
                .departureDate(LocalDateTime.of(2021, Month.NOVEMBER, 9, 1, 10, 54))
                .zoneOffset("+01:00")
                .build();

        CargoItem cargoItem1 = CargoItem.builder().id(901L).weight(150d).weightUnit(WeightUnit.POUND).pieces(5).build();
        CargoItem cargoItem2 = CargoItem.builder().id(902L).weight(220d).weightUnit(WeightUnit.KILOGRAM).pieces(10).build();
        CargoItem cargoItem3 = CargoItem.builder().id(903L).weight(322d).weightUnit(WeightUnit.POUND).pieces(100).build();
        CargoItem cargoItem4 = CargoItem.builder().id(904L).weight(620d).weightUnit(WeightUnit.KILOGRAM).pieces(3).build();
        CargoItem cargoItem5 = CargoItem.builder().id(905L).weight(270d).weightUnit(WeightUnit.KILOGRAM).pieces(213).build();

        // prepare
        when(flightRepository.findAllByFlightNumberAndDate(flightNumber, date))
                .thenReturn(List.of(f1));
        when(cargoItemRepository.findAllByFlightIdAndType(f1.getId(), CargoType.CARGO))
                .thenReturn(List.of(cargoItem1, cargoItem2, cargoItem3));
        when(cargoItemRepository.findAllByFlightIdAndType(f1.getId(), CargoType.BAGGAGE))
                .thenReturn(List.of(cargoItem4, cargoItem5));

        // execute
        List<FlightWeightInfo> flightWeightInfoList = flightService.getFlightWeightInfo(flightNumber, date, weightUnit);

        assertEquals(1, flightWeightInfoList.size());

        Double expectedCargoWeight = WeightUnit.convert(cargoItem1.getWeight(), cargoItem1.getWeightUnit(), weightUnit)
                                        + cargoItem2.getWeight()
                                        + WeightUnit.convert(cargoItem3.getWeight(), cargoItem3.getWeightUnit(), weightUnit);
        Double expectedBaggageWeight = cargoItem4.getWeight() + cargoItem5.getWeight();
        Double expectedTotalWeight = Double.sum(expectedCargoWeight, expectedBaggageWeight);

        FlightWeightInfo flightWeightInfo = flightWeightInfoList.get(0);
        assertEquals(expectedCargoWeight, flightWeightInfo.getCargo());
        assertEquals(expectedBaggageWeight, flightWeightInfo.getBaggage());
        assertEquals(expectedTotalWeight, flightWeightInfo.getTotal());
    }

    @Test
    public void whenNonExistingIATACodeAndDateArePassed_thenExceptionIsThrown() {
        // parameters
        String iataCode = "LAX";
        Date date = new Date(2021, Calendar.NOVEMBER, 9);

        // prepare
        when(flightRepository.findAllByAirportCodeAndDate(iataCode, date))
                .thenReturn(new ArrayList<>());

        // execute
        assertThrows(FlightException.class, () -> flightService.getAirportInfo(iataCode, date));
    }

    @Test
    public void whenExistingIATACodeAndDateArePassed_thenAirportInfoIsReturned() {
        // parameters
        String iataCode = "LAX";
        Date date = new Date(2021, Calendar.NOVEMBER, 9);

        // data
        Flight f1 = Flight.builder()
                .id(997L)
                .flightNumber(1111)
                .departureAirportIATACode(iataCode)
                .arrivalAirportIATACode("INI")
                .departureDate(LocalDateTime.of(2021, Month.NOVEMBER, 9, 1, 10, 54))
                .zoneOffset("+01:00")
                .build();

        Flight f2 = Flight.builder()
                .id(998L)
                .flightNumber(2222)
                .departureAirportIATACode("MAD")
                .arrivalAirportIATACode(iataCode)
                .departureDate(LocalDateTime.of(2021, Month.NOVEMBER, 9, 13, 52, 17))
                .zoneOffset("-05:00")
                .build();

        CargoItem cargoItem1 = CargoItem.builder().id(901L).weight(150d).weightUnit(WeightUnit.POUND).pieces(5).build();
        CargoItem cargoItem2 = CargoItem.builder().id(902L).weight(220d).weightUnit(WeightUnit.KILOGRAM).pieces(10).build();
        CargoItem cargoItem3 = CargoItem.builder().id(903L).weight(322d).weightUnit(WeightUnit.POUND).pieces(100).build();
        CargoItem cargoItem4 = CargoItem.builder().id(904L).weight(620d).weightUnit(WeightUnit.KILOGRAM).pieces(3).build();
        CargoItem cargoItem5 = CargoItem.builder().id(905L).weight(270d).weightUnit(WeightUnit.KILOGRAM).pieces(213).build();

        // prepare
        when(flightRepository.findAllByAirportCodeAndDate(iataCode, date))
                .thenReturn(List.of(f1, f2));
        when(cargoItemRepository.findAllByFlightIdAndType(f1.getId(), CargoType.BAGGAGE))
                .thenReturn(List.of(cargoItem1, cargoItem2, cargoItem3));
        when(cargoItemRepository.findAllByFlightIdAndType(f2.getId(), CargoType.BAGGAGE))
                .thenReturn(List.of(cargoItem4, cargoItem5));

        // execute
        AirportInfo airportInfo = flightService.getAirportInfo(iataCode, date);

        assertNotNull(airportInfo);

        Integer expectedNumberOfDepartures = 1;
        Integer expectedNumberOfArrivals = 1;
        Integer expectedNumberOfBaggageArriving = cargoItem4.getPieces() + cargoItem5.getPieces();
        Integer expectedNumberOfBaggageDeparting = cargoItem1.getPieces() + cargoItem2.getPieces() + cargoItem3.getPieces();

        assertEquals(expectedNumberOfDepartures, airportInfo.getNumberOfDepartures());
        assertEquals(expectedNumberOfArrivals, airportInfo.getNumberOfArrivals());
        assertEquals(expectedNumberOfBaggageArriving, airportInfo.getNumberOfBaggageArriving());
        assertEquals(expectedNumberOfBaggageDeparting, airportInfo.getNumberOfBaggageDeparting());
    }
}
