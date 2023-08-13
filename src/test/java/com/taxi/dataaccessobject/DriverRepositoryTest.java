package com.taxi.dataaccessobject;

import com.taxi.controller.specification.DriverSpecification;
import com.taxi.domainobject.CarDO;
import com.taxi.domainobject.DriverDO;
import com.taxi.domainvalue.OnlineStatus;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the driver data access layer.
 * <p/>
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class DriverRepositoryTest
{
    @Autowired
    private DriverRepository driverRepository;


    /**
     * Find driver by id.
     */
    @Test
    void find_driver_by_id()
    {
        DriverDO fetchedDriver = driverRepository.findById(1L).get();
        assertEquals(1, fetchedDriver.getId());
    }


    /**
     * Find drivers by online status.
     */
    @Test
    void find_by_online_status()
    {
        var status = OnlineStatus.ONLINE;
        var expected = OnlineStatus.ONLINE;

        List<DriverDO> fetchedDriver = driverRepository.findByOnlineStatus(status);

        assertTrue(fetchedDriver.size() > 0);
        assertEquals(expected, fetchedDriver.get(0).getOnlineStatus());
    }


    /**
     * Find first driver linked with car.
     */
    @Test
    void find_first_by_car()
    {
        var carID = 1L;
        var car = new CarDO();
        car.setId(carID);

        DriverDO fetchedDriver = driverRepository.findFirstByCar(car);

        assertNotNull(fetchedDriver);
        assertEquals(carID, fetchedDriver.getCar().getId());
    }


    /**
     * Find all drivers with driver and car attributes and operations defined in DriverSpecification.
     */
    @Test
    void find_by_specs()
    {
        DriverSpecification specs = new DriverSpecification();
        specs.setUsername("driver01");
        specs.setOnlineStatus(OnlineStatus.OFFLINE);
        specs.setLicensePlate("car01");
        specs.setSeatCount(4);

        List<DriverDO> fetchedDriver = driverRepository.findAll(specs);

        assertNotNull(fetchedDriver);
        assertEquals(1, fetchedDriver.get(0).getCar().getId());
    }
}
