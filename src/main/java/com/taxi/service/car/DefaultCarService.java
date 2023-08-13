package com.taxi.service.car;

import com.taxi.dataaccessobject.CarRepository;
import com.taxi.domainobject.CarDO;
import com.taxi.domainvalue.EngineType;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some car specific things.
 * <p/>
 */
@Service
public class DefaultCarService implements CarService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;


    public DefaultCarService(final CarRepository carRepository)
    {
        this.carRepository = carRepository;
    }


    /**
     * select var by id.
     *
     * @param carId
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public CarDO find(Long carId) throws EntityNotFoundException
    {
        return findCarChecked(carId);
    }


    /**
     * create a new car.
     *
     * @param carDO
     * @return
     * @throws ConstraintsViolationException if a car already exists with the given licensePlate, ..
     */
    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException
    {
        CarDO car;
        try
        {
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a car: {}", carDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    /**
     * Deletes an existing car by id
     *
     * @param carId
     * @return
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    @Transactional
    public CarDO delete(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = findCarChecked(carId);
        carDO.setDeleted(true);
        return carDO;
    }


    /**
     * Update the rating for a car.
     *
     * @param carId
     * @param rating
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public CarDO updateRating(Long carId, double rating) throws EntityNotFoundException
    {
        CarDO carDO = findCarChecked(carId);
        carDO.setRating(rating);
        return carDO;
    }


    /**
     * Find all cars by engine type.
     *
     * @param engineType
     * @return
     */
    @Override
    public List<CarDO> find(EngineType engineType)
    {
        return carRepository.findByEngineType(engineType);
    }


    @Override
    public CarDO findCarChecked(Long carId) throws EntityNotFoundException
    {
        return carRepository.findById(carId)
            .orElseThrow(() -> new EntityNotFoundException("Could not find entity car with id: " + carId));
    }
}
