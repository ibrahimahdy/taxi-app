package com.taxi.service.driver;

import com.taxi.controller.specification.DriverSpecification;
import com.taxi.domainobject.DriverDO;
import com.taxi.domainvalue.OnlineStatus;
import com.taxi.exception.CarAlreadyInUseException;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import java.util.List;

public interface DriverService
{

    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    DriverDO updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;

    List<DriverDO> find(OnlineStatus onlineStatus);

    DriverDO selectCar(Long driverId, Long carId) throws EntityNotFoundException, CarAlreadyInUseException;

    DriverDO deselectCar(Long driverId) throws EntityNotFoundException;

    List<DriverDO> search(DriverSpecification params);

}
