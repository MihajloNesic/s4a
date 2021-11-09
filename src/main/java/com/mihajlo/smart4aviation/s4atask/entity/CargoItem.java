package com.mihajlo.smart4aviation.s4atask.entity;

import com.mihajlo.smart4aviation.s4atask.dto.CargoDto;
import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CargoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double weight;
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;
    private Integer pieces;

    public static CargoItem createFromDto(CargoDto.CargoItemDto dto) {
        return CargoItem.builder()
                .id(dto.getId())
                .weight(dto.getWeight())
                .weightUnit(dto.getWeightUnit())
                .pieces(dto.getPieces())
                .build();
    }

    public CargoItem convertToWeightUnit(WeightUnit weightUnit) {
        if (this.weightUnit.equals(weightUnit)) {
            return this;
        }
        double newWeight = WeightUnit.convert(this.weight, this.weightUnit, weightUnit);
        this.setWeight(newWeight);
        this.setWeightUnit(weightUnit);
        return this;
    }

    @Override
    public String toString() {
        return "CargoItem{" +
                "id=" + id +
                ", weight=" + weight +
                ", weightUnit=" + weightUnit +
                ", pieces=" + pieces +
                '}';
    }
}
