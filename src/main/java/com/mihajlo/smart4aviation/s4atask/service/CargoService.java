package com.mihajlo.smart4aviation.s4atask.service;

import com.mihajlo.smart4aviation.s4atask.entity.domain.CargoType;
import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;

public interface CargoService {
    Double calculateFlightCargo(Long flightId, CargoType cargoType, WeightUnit weightUnit);
    Integer calculateFlightCargoNumber(Long flightId, CargoType cargoType);
}
