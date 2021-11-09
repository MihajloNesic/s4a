package com.mihajlo.smart4aviation.s4atask.repositry;

import com.mihajlo.smart4aviation.s4atask.entity.CargoItem;
import com.mihajlo.smart4aviation.s4atask.entity.domain.CargoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoItemRepository extends JpaRepository<CargoItem, Long> {

    @Query("select c.cargoItem from " +
            "Cargo c " +
            "where c.flight.id = :flightId " +
            "and c.cargoType = :cargoType")
    List<CargoItem> findAllByFlightIdAndType(@Param("flightId") Long flightId, @Param("cargoType") CargoType cargoType);
}
