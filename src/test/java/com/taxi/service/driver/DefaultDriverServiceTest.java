package com.taxi.service.driver;

import com.taxi.controller.specification.DriverSpecification;
import com.taxi.dataaccessobject.DriverRepository;
import com.taxi.domainvalue.GeoCoordinate;
import com.taxi.domainvalue.OnlineStatus;
import com.taxi.exception.CarAlreadyInUseException;
import com.taxi.exception.ConstraintsViolationException;
import com.taxi.exception.EntityNotFoundException;
import com.taxi.service.car.CarService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static com.taxi.common.TestFixtures.CAR_ID;
import static com.taxi.common.TestFixtures.DRIVER_ID;
import static com.taxi.common.TestFixtures.aCarDO;
import static com.taxi.common.TestFixtures.aDeletedDriverDO;
import static com.taxi.common.TestFixtures.aDriverDO;
import static com.taxi.common.TestFixtures.aDriverWithCarDO;
import static com.taxi.common.TestFixtures.anOfflineDriverWithCarDO;
import static com.taxi.common.TestFixtures.anOnlineDriverDO;
import static com.taxi.common.TestFixtures.password;
import static com.taxi.common.TestFixtures.username;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the driver business layer service.
 * <p/>
 */
@ExtendWith(MockitoExtension.class)
class DefaultDriverServiceTest
{

    @Mock
    private DriverRepository driverRepository;
    @Mock
    private CarService carService;
    @InjectMocks
    private DefaultDriverService defaultDriverService;


    /**
     * Find driver by id.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void find_driver_by_id() throws EntityNotFoundException
    {
        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(aDriverDO()));

        var driver = defaultDriverService.find(DRIVER_ID);

        assertEquals(username, driver.getUsername());
        assertEquals(password, driver.getPassword());
    }


    /**
     * Find a non-existing driver by id throws an EntityNotFoundException
     */
    @Test
    void find_non_existing_driver_by_id()
    {
        var message = "Could not find entity with id: " + DRIVER_ID;
        var exception = assertThrows(EntityNotFoundException.class, () -> defaultDriverService.find(DRIVER_ID));

        assertEquals(message, exception.getMessage());
    }


    /**
     * Create a driver.
     *
     * @throws ConstraintsViolationException
     */
    @Test
    void create_driver() throws ConstraintsViolationException
    {
        when(driverRepository.save(any())).thenReturn(aDriverDO());

        var driver = defaultDriverService.create(aDriverDO());

        assertEquals(username, driver.getUsername());
        assertEquals(password, driver.getPassword());
    }


    /**
     * Create a driver when already one has the same username throws a ConstraintsViolationException.
     */
    @Test
    void create_driver_not_allowed_when_violating_constraint()
    {
        when(driverRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        var exception = assertThrows(ConstraintsViolationException.class, () -> defaultDriverService.create(aDriverDO()));

        assertEquals(ConstraintsViolationException.class, exception.getClass());
    }


    /**
     * Delete driver by id.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void delete_driver() throws EntityNotFoundException
    {
        when(driverRepository.findById(any())).thenReturn(Optional.of(aDeletedDriverDO()));
        defaultDriverService.delete(DRIVER_ID);

        verify(driverRepository, times(1)).findById(DRIVER_ID);
    }


    /**
     * Update driver location.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void update_location() throws EntityNotFoundException
    {
        double longitude = 1.1;
        double latitude = 2.2;
        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(aDriverDO()));

        var driver = defaultDriverService.updateLocation(DRIVER_ID, longitude, latitude);

        assertEquals(new GeoCoordinate(latitude, longitude), driver.getCoordinate());
    }


    /**
     * Find drivers by online status.
     */
    @Test
    void find_by_online_status()
    {
        OnlineStatus status = OnlineStatus.ONLINE;
        when(driverRepository.findByOnlineStatus(status)).thenReturn(List.of(anOnlineDriverDO()));

        var driver = defaultDriverService.find(status);

        assertEquals(1, driver.size());
        assertEquals(OnlineStatus.ONLINE, driver.get(0).getOnlineStatus());
    }


    /**
     * Select a car for driver.
     *
     * @throws EntityNotFoundException
     * @throws CarAlreadyInUseException
     */
    @Test
    void select_car_for_driver() throws EntityNotFoundException, CarAlreadyInUseException
    {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(aDriverDO()));
        when(carService.findCarChecked(CAR_ID)).thenReturn(aCarDO());
        when(driverRepository.findFirstByCar(any())).thenReturn(null);

        var driver = defaultDriverService.selectCar(DRIVER_ID, CAR_ID);
        var car = driver.getCar();

        assertEquals(aCarDO().getLicensePlate(), car.getLicensePlate());
    }


    /**
     * Select an available car for driver throws CarAlreadyInUseException.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void select_unavailable_car_for_driver_not_possible() throws EntityNotFoundException
    {
        var message = "Car is already used by another driver!";
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(aDriverDO()));
        when(carService.findCarChecked(CAR_ID)).thenReturn(aCarDO());
        when(driverRepository.findFirstByCar(any())).thenReturn(anOnlineDriverDO());

        var exception = assertThrows(CarAlreadyInUseException.class, () -> defaultDriverService.selectCar(DRIVER_ID, CAR_ID));

        assertEquals(CarAlreadyInUseException.class, exception.getClass());
        assertEquals(exception.getMessage(), message);
    }


    /**
     * Select car previously linked with offline driver.
     *
     * @throws EntityNotFoundException
     * @throws CarAlreadyInUseException
     */
    @Test
    void select_unused_car_for_driver() throws EntityNotFoundException, CarAlreadyInUseException
    {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(aDriverDO()));
        when(carService.findCarChecked(CAR_ID)).thenReturn(aCarDO());
        when(driverRepository.findFirstByCar(any())).thenReturn(anOfflineDriverWithCarDO());

        var driver = defaultDriverService.selectCar(DRIVER_ID, CAR_ID);
        var car = driver.getCar();

        assertEquals(aCarDO().getLicensePlate(), car.getLicensePlate());
    }


    /**
     * Deselect a car for driver.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void deselect_car() throws EntityNotFoundException
    {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(aDriverWithCarDO()));

        var driver = defaultDriverService.deselectCar(DRIVER_ID);

        assertNull(driver.getCar());
    }


    /**
     * Find all drivers with driver and car attributes and operations defined in DriverSpecification.
     */
    @Test
    void search_drivers()
    {
        DriverSpecification params = new DriverSpecification();
        params.setUsername(username);
        when(driverRepository.findAll(params)).thenReturn(List.of(aDriverDO()));

        var driversList = defaultDriverService.search(params);

        assertEquals(1, driversList.size());
        assertEquals(username, driversList.get(0).getUsername());
    }
}
