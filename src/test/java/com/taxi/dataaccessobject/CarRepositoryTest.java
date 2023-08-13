package com.taxi.dataaccessobject;

import com.taxi.domainobject.CarDO;
import com.taxi.domainvalue.EngineType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.taxi.common.TestFixtures.aCarDO;
import static com.taxi.common.TestFixtures.licensePlate;
import static com.taxi.common.TestFixtures.seatCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the car data access layer.
 * <p/>
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CarRepositoryTest
{
    @Autowired
    private CarRepository carRepository;


    /**
     * Find car by id.
     */
    @Test
    void find_car_by_id()
    {
        CarDO fetchedCar = carRepository.findById(1L).get();
        assertEquals(1, fetchedCar.getId());
    }


    /**
     * Save a car.
     */
    @Test
    void save_car()
    {
        CarDO carDO = aCarDO();

        CarDO savedCar = carRepository.save(carDO);

        assertEquals(carDO.getLicensePlate(), savedCar.getLicensePlate());
        assertEquals(carDO.getSeatCount(), savedCar.getSeatCount());
    }


    /**
     * Save a car when already one has the same licence plate throws DataIntegrityViolationException.
     */
    @Test
    void save_car_not_allowed_when_violating_constraint()
    {
        CarDO carDO = new CarDO(licensePlate, seatCount);
        CarDO carDO2 = new CarDO(licensePlate, 5);

        carRepository.save(carDO);
        var exception = assertThrows(DataIntegrityViolationException.class, () -> carRepository.save(carDO2));

        assertEquals(DataIntegrityViolationException.class, exception.getClass());
    }


    /**
     * Find cars by engine type.
     */
    @Test
    void find_by_engine_type()
    {
        var type = EngineType.ELECTRIC;
        var expected = EngineType.ELECTRIC;

        List<CarDO> fetchedCars = carRepository.findByEngineType(type);

        assertTrue(fetchedCars.size() > 0);
        assertEquals(expected, fetchedCars.get(0).getEngineType());
    }
}
