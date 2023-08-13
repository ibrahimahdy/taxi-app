package com.taxi.controller;

import com.taxi.controller.mapper.CarMapper;
import com.taxi.datatransferobject.CarDTO;
import com.taxi.domainobject.CarDO;
import com.taxi.domainvalue.EngineType;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import com.taxi.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/cars")
public class CarController
{

    private final CarService carService;

    @Autowired
    public CarController(CarService carService)
    {
        this.carService = carService;
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarDTO> getCar(@PathVariable long carId) throws EntityNotFoundException
    {
        return ResponseEntity.ok(CarMapper.makeCarDTO(carService.find(carId)));
    }

    @PostMapping
    public ResponseEntity<CarDTO> createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        CarDTO response = CarMapper.makeCarDTO(carService.create(carDO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<CarDTO> deleteCar(@PathVariable long carId) throws EntityNotFoundException
    {
        carService.delete(carId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{carId}")
    public ResponseEntity<CarDTO> updateRating(@PathVariable long carId, @RequestParam double rating) throws EntityNotFoundException
    {
        return ResponseEntity.ok(CarMapper.makeCarDTO(carService.updateRating(carId, rating)));
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> findCars(@RequestParam EngineType engineType)
    {
        return ResponseEntity.ok(CarMapper.makeCarDTOList(carService.find(engineType)));
    }
}
