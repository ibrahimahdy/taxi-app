package com.taxi.service.car;

import com.taxi.dataaccessobject.CarRepository;
import com.taxi.domainvalue.EngineType;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static com.taxi.common.TestFixtures.CAR_ID;
import static com.taxi.common.TestFixtures.aCarDO;
import static com.taxi.common.TestFixtures.anElectricCarDO;
import static com.taxi.common.TestFixtures.licensePlate;
import static com.taxi.common.TestFixtures.seatCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the car business layer service.
 * <p/>
 */
@ExtendWith(MockitoExtension.class)
class DefaultCarServiceTest
{

    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private DefaultCarService defaultCarService;


    /**
     * Find car by id.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void find_car_by_id() throws EntityNotFoundException
    {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(aCarDO()));

        var car = defaultCarService.find(CAR_ID);

        assertEquals(licensePlate, car.getLicensePlate());
        assertEquals(seatCount, car.getSeatCount());
    }


    /**
     * Find a non-existing car by id throws an EntityNotFoundException
     */
    @Test
    void find_non_existing_car_by_id_not_possible()
    {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());
        var message = "Could not find entity car with id: " + CAR_ID;

        var exception = assertThrows(EntityNotFoundException.class, () -> defaultCarService.find(CAR_ID));

        assertEquals(message, exception.getMessage());
    }


    /**
     * Create a car.
     *
     * @throws ConstraintsViolationException
     */
    @Test
    void create_car() throws ConstraintsViolationException
    {
        when(carRepository.save(any())).thenReturn(aCarDO());

        var car = defaultCarService.create(aCarDO());

        assertEquals(licensePlate, car.getLicensePlate());
        assertEquals(seatCount, car.getSeatCount());
    }


    /**
     * Create a car when already one has the same licence plate throws ConstraintsViolationException.
     */
    @Test
    void create_car_not_allowed_when_violating_constraint()
    {
        when(carRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        var exception = assertThrows(ConstraintsViolationException.class, () -> defaultCarService.create(aCarDO()));

        assertEquals(ConstraintsViolationException.class, exception.getClass());
    }


    /**
     * Delete car by id.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void delete_driver() throws EntityNotFoundException
    {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(aCarDO()));

        var car = defaultCarService.delete(CAR_ID);

        verify(carRepository, times(1)).findById(CAR_ID);
        assertTrue(car.getDeleted());
    }


    /**
     * Update car rating.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void update_rating() throws EntityNotFoundException
    {
        double rating = 5;
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(aCarDO()));

        var car = defaultCarService.updateRating(CAR_ID, rating);

        assertEquals(5, car.getRating());
    }


    /**
     * Find cars by engine type.
     */
    @Test
    void find_by_engine_type()
    {
        EngineType type = EngineType.ELECTRIC;
        when(carRepository.findByEngineType(any())).thenReturn(List.of(anElectricCarDO()));

        var driver = defaultCarService.find(type);

        assertEquals(1, driver.size());
        assertEquals(EngineType.ELECTRIC, driver.get(0).getEngineType());
    }
}
