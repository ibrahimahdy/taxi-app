package com.taxi.service.driver;

import com.taxi.controller.specification.DriverSpecification;
import com.taxi.dataaccessobject.DriverRepository;
import com.taxi.domainobject.CarDO;
import com.taxi.domainobject.DriverDO;
import com.taxi.domainvalue.GeoCoordinate;
import com.taxi.domainvalue.OnlineStatus;
import com.taxi.exception.CarAlreadyInUseException;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import com.taxi.service.car.CarService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;

    private final CarService carService;


    public DefaultDriverService(final DriverRepository driverRepository, CarService carService)
    {
        this.driverRepository = driverRepository;
        this.carService = carService;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public DriverDO updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
        return driverDO;
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {
        return driverRepository.findByOnlineStatus(onlineStatus);
    }


    /**
     * Assign the car given by the car id to the driver given by the driver id.
     * A car assigned to an offline driver is re-assigned to the driver given by the driver id.
     * An offline driver can not select a car.
     *
     * @param driverId
     * @param carId
     * @return
     * @throws EntityNotFoundException
     * @throws CarAlreadyInUseException if the car with the given id has already be selected by another online driver.
     */
    @Override
    @Transactional
    public DriverDO selectCar(Long driverId, Long carId) throws EntityNotFoundException, CarAlreadyInUseException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        CarDO carDO = carService.findCarChecked(carId);

        DriverDO currentDriver = driverRepository.findFirstByCar(carDO);

        if (currentDriver != null && currentDriver.getOnlineStatus() == OnlineStatus.ONLINE)
        {
            throw new CarAlreadyInUseException("Car is already used by another driver!");
        }
        else if (currentDriver != null)
        {
            currentDriver.setCar(null);
        }

        driverDO.setCar(carDO);

        return driverDO;
    }

    /**
     * Driver deselects the car associated with driver.
     *
     * @param driverId
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public DriverDO deselectCar(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCar(null);
        return driverDO;
    }


    /**
     * Find all driver by DriverSpecification.
     *
     * @param params
     * @return
     */
    @Override
    public List<DriverDO> search(DriverSpecification params)
    {
        return driverRepository.findAll(params);
    }


    /**
     * @param driverId
     * @return
     * @throws EntityNotFoundException
     */
    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException
    {
        return driverRepository.findById(driverId)
            .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
    }

}
