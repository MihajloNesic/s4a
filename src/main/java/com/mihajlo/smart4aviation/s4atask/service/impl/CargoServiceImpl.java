package com.mihajlo.smart4aviation.s4atask.service.impl;

import com.mihajlo.smart4aviation.s4atask.entity.CargoItem;
import com.mihajlo.smart4aviation.s4atask.entity.domain.CargoType;
import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;
import com.mihajlo.smart4aviation.s4atask.repositry.CargoItemRepository;
import com.mihajlo.smart4aviation.s4atask.service.CargoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CargoServiceImpl implements CargoService {

    private CargoItemRepository cargoItemRepository;

    @Override
    public Double calculateFlightCargo(Long flightId, CargoType cargoType, WeightUnit weightUnit) {
        return cargoItemRepository.findAllByFlightIdAndType(flightId, cargoType).stream()
                .mapToDouble(ci -> WeightUnit.convert(ci.getWeight(), ci.getWeightUnit(), weightUnit))
                .sum();
    }

    @Override
    public Integer calculateFlightCargoNumber(Long flightId, CargoType cargoType) {
        return cargoItemRepository.findAllByFlightIdAndType(flightId, cargoType).stream()
                .mapToInt(CargoItem::getPieces)
                .sum();
    }
}
