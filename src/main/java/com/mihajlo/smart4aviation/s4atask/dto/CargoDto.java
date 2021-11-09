package com.mihajlo.smart4aviation.s4atask.dto;

import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;
import lombok.Data;
import java.util.List;

@Data
public class CargoDto {

    private Long flightId;
    private List<CargoItemDto> baggage;
    private List<CargoItemDto> cargo;

    @Data
    public static class CargoItemDto {
        private Long id;
        private Double weight;
        private WeightUnit weightUnit;
        private Integer pieces;
    }
}