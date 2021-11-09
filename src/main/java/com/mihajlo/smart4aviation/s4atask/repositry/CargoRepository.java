package com.mihajlo.smart4aviation.s4atask.repositry;

import com.mihajlo.smart4aviation.s4atask.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

}
