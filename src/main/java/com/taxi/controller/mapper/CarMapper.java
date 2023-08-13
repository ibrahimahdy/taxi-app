package com.taxi.controller.mapper;

import com.taxi.datatransferobject.CarDTO;
import com.taxi.domainobject.CarDO;
import com.taxi.domainvalue.EngineType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Mapping domain object to data access object and vice versa.
 * <p/>
 */
@Component
public final class CarMapper
{

    private CarMapper()
    {
    }


    public static CarDO makeCarDO(CarDTO carDTO)
    {
        CarDO carDO = new CarDO(carDTO.getLicensePlate(), carDTO.getSeatCount());
        carDO.setRating(carDTO.getRating());
        carDO.setConvertible(carDTO.getConvertible());

        if (carDTO.getEngineType() != null)
        {
            carDO.setEngineType(EngineType.valueOf(carDTO.getEngineType()));
        }

        return carDO;
    }


    public static CarDTO makeCarDTO(CarDO carDO)
    {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
            .setId(carDO.getId())
            .setLicensePlate(carDO.getLicensePlate())
            .setSeatCount(carDO.getSeatCount())
            .setRating(carDO.getRating())
            .setConvertible(carDO.getConvertible());

        if (carDO.getEngineType() != null)
        {
            carDTOBuilder.setEngineType(carDO.getEngineType().toString());
        }

        if (carDO.getManufacturer() != null)
        {
            carDTOBuilder.setManufacturer(ManufacturerMapper.makeManufacturerDTO(carDO.getManufacturer()));
        }

        return carDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars)
    {
        return cars.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}
