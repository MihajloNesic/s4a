package com.mihajlo.smart4aviation.s4atask.repositry;

import com.mihajlo.smart4aviation.s4atask.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("select f " +
           "from Flight f " +
           "where f.flightNumber = :flightNumber " +
           "and cast(f.departureDate as date) = :date")
    List<Flight> findAllByFlightNumberAndDate(@Param("flightNumber") Integer flightNumber, @Param("date") Date date);

    @Query("select f " +
           "from Flight f " +
           "where (f.departureAirportIATACode = :iataCode or f.arrivalAirportIATACode = :iataCode) " +
           "and cast(f.departureDate as date) = :date")
    List<Flight> findAllByAirportCodeAndDate(@Param("iataCode") String iataCode, @Param("date") Date date);
}
