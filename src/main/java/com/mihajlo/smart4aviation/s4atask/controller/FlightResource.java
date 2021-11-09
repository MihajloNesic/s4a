package com.mihajlo.smart4aviation.s4atask.controller;

import com.mihajlo.smart4aviation.s4atask.dto.AirportInfo;
import com.mihajlo.smart4aviation.s4atask.dto.FlightWeightInfo;
import com.mihajlo.smart4aviation.s4atask.entity.domain.WeightUnit;
import com.mihajlo.smart4aviation.s4atask.service.FlightService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightResource {

    @Autowired
    private FlightService flightService;

    @GetMapping("/weight-info")
    public ResponseEntity<List<FlightWeightInfo>> getFlightWeightInfo(@RequestParam("flightNumber") @ApiParam(example = "1121") Integer flightNumber,
                                                                     @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam(example = "2017-10-07") Date date,
                                                                     @RequestParam(value = "weightUnit", required = false, defaultValue = "KILOGRAM") @ApiParam(example = "KILOGRAM") WeightUnit weightUnit) {
        return ResponseEntity.ok(flightService.getFlightWeightInfo(flightNumber, date, weightUnit));
    }

    @GetMapping("/airport-info")
    public ResponseEntity<AirportInfo> getAirportInfo(@RequestParam("iataCode") @ApiParam(example = "LAX") String iataCode,
                                                      @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam(example = "2018-03-14") Date date) {
        return ResponseEntity.ok(flightService.getAirportInfo(iataCode, date));
    }

}
