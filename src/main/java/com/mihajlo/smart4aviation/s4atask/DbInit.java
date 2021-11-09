package com.mihajlo.smart4aviation.s4atask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mihajlo.smart4aviation.s4atask.dto.CargoDto;
import com.mihajlo.smart4aviation.s4atask.dto.FlightDto;
import com.mihajlo.smart4aviation.s4atask.entity.Cargo;
import com.mihajlo.smart4aviation.s4atask.entity.CargoItem;
import com.mihajlo.smart4aviation.s4atask.entity.Flight;
import com.mihajlo.smart4aviation.s4atask.entity.domain.CargoType;
import com.mihajlo.smart4aviation.s4atask.repositry.CargoItemRepository;
import com.mihajlo.smart4aviation.s4atask.repositry.CargoRepository;
import com.mihajlo.smart4aviation.s4atask.repositry.FlightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DbInit {

    @Value("classpath:Flight.json")
    private Resource flightResource;
    @Value("classpath:Cargo.json")
    private Resource cargoResource;

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private CargoItemRepository cargoItemRepository;
    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {
        log.info("Init db started");
        loadFlights();
        loadCargo();
        log.info("Init db finished");
    }

    private void loadFlights() throws IOException {
        List<FlightDto> flightDtos = jsonFileToDto(flightResource.getFile(), FlightDto.class);
        List<Flight> flights = flightDtos.stream().map(Flight::createFromDto).collect(Collectors.toList());
        flightRepository.saveAll(flights);
        flightRepository.flush();
        log.info("flights: ");
        flights.forEach(f -> log.info(f.toString()));
    }

    private void loadCargo() throws IOException {
        List<CargoDto> cargoDtos = jsonFileToDto(cargoResource.getFile(), CargoDto.class);

        List<CargoItem> cargoItems = new ArrayList<>();
        List<Cargo> cargos = new ArrayList<>();

        for (CargoDto cargoDto : cargoDtos) {
            Flight flight = flightRepository.findById(cargoDto.getFlightId()).get();

            for (CargoDto.CargoItemDto itemBaggage : cargoDto.getBaggage()) {
                CargoItem cargoItem = CargoItem.createFromDto(itemBaggage);
                Cargo cargo = Cargo.builder()
                        .flight(flight)
                        .cargoItem(cargoItem)
                        .cargoType(CargoType.BAGGAGE)
                        .build();

                cargoItems.add(cargoItem);
                cargos.add(cargo);
            }

            for (CargoDto.CargoItemDto itemCargo : cargoDto.getCargo()) {
                CargoItem cargoItem = CargoItem.createFromDto(itemCargo);
                Cargo cargo = Cargo.builder()
                        .flight(flight)
                        .cargoItem(cargoItem)
                        .cargoType(CargoType.CARGO)
                        .build();

                cargoItems.add(cargoItem);
                cargos.add(cargo);
            }

        }

        cargoItemRepository.saveAll(cargoItems);
        cargoRepository.saveAll(cargos);

        log.info("cargo items: ");
        cargoItems.forEach(ci -> log.info(ci.toString()));

        log.info("cargo: ");
        cargos.forEach(c -> log.info(c.toString()));
    }

    private <T> List<T> jsonFileToDto(File jsonFile, Class<T> dtoClass) throws IOException {
        CollectionType arrayCollection = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, dtoClass);
        String jsonString = new String(Files.readAllBytes(jsonFile.toPath()));
        return objectMapper.readValue(jsonString, arrayCollection);
    }
}
