package com.taxi.controller;

import com.taxi.controller.mapper.DriverMapper;
import com.taxi.controller.specification.DriverSpecification;
import com.taxi.datatransferobject.DriverDTO;
import com.taxi.domainobject.DriverDO;
import com.taxi.domainvalue.OnlineStatus;
import com.taxi.exception.CarAlreadyInUseException;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import com.taxi.service.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;

    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        return ResponseEntity.ok(DriverMapper.makeDriverDTO(driverService.find(driverId)));
    }

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        DriverDTO response = DriverMapper.makeDriverDTO(driverService.create(driverDO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<DriverDTO> updateLocation(@PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude) throws EntityNotFoundException
    {
        return ResponseEntity.ok(DriverMapper.makeDriverDTO(driverService.updateLocation(driverId, longitude, latitude)));
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> findDrivers(@RequestParam OnlineStatus onlineStatus)
    {
        return ResponseEntity.ok(DriverMapper.makeDriverDTOList(driverService.find(onlineStatus)));
    }

    @PutMapping("/{driverId}/cars/select/{carId}")
    public ResponseEntity<DriverDTO> selectCar(@PathVariable long driverId, @PathVariable long carId) throws EntityNotFoundException, CarAlreadyInUseException
    {
        return ResponseEntity.ok(DriverMapper.makeDriverDTO(driverService.selectCar(driverId, carId)));
    }

    @DeleteMapping("/{driverId}/cars/deselect")
    public ResponseEntity<DriverDTO> deselectCar(@PathVariable long driverId) throws EntityNotFoundException
    {
        return ResponseEntity.ok(DriverMapper.makeDriverDTO(driverService.deselectCar(driverId)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DriverDTO>> searchDrivers(DriverSpecification params)
    {
        return ResponseEntity.ok(DriverMapper.makeDriverDTOList(driverService.search(params)));
    }
}
