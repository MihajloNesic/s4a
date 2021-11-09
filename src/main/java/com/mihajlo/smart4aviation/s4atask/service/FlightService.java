package com.mihajlo.smart4aviation.s4atask.service;

import com.mihajlo.smart4aviation.s4atask.dto.AirportInfo;
import com.mihajlo.smart4aviation.s4atask.dto.FlightWeightInfo;
import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;

import java.util.Date;
import java.util.List;

public interface FlightService {
    List<FlightWeightInfo> getFlightWeightInfo(Integer flightNumber, Date date, WeightUnit weightUnit);
    AirportInfo getAirportInfo(String iataCode, Date date);
}
