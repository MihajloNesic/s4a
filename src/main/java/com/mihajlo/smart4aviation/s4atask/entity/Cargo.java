package com.mihajlo.smart4aviation.s4atask.entity;

import com.mihajlo.smart4aviation.s4atask.entity.domain.CargoType;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "cargo_item_id", referencedColumnName = "id")
    private CargoItem cargoItem;

    @Enumerated(EnumType.STRING)
    private CargoType cargoType;

    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", flightId=" + flight.getId() +
                ", cargoItemId=" + cargoItem.getId()+
                ", cargoType=" + cargoType +
                '}';
    }
}
