package com.taxi.service.car;

import com.taxi.domainobject.CarDO;
import com.taxi.domainvalue.EngineType;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import java.util.List;

public interface CarService
{
    CarDO find(Long carId) throws EntityNotFoundException;

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    CarDO delete(Long carId) throws EntityNotFoundException;

    CarDO updateRating(Long carId, double rating) throws EntityNotFoundException;

    CarDO findCarChecked(Long carId) throws EntityNotFoundException;

    List<CarDO> find(EngineType engineType);
}
