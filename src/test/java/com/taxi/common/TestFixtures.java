package com.taxi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxi.datatransferobject.CarDTO;
import com.taxi.datatransferobject.DriverDTO;
import com.taxi.domainobject.CarDO;
import com.taxi.domainobject.DriverDO;
import com.taxi.domainvalue.EngineType;
import com.taxi.domainvalue.GeoCoordinate;
import com.taxi.domainvalue.OnlineStatus;

/**
 * Defines drivers and cars fixtures to be used for test cases.
 * <p/>
 */
public class TestFixtures
{
    public static final Long DRIVER_ID = 1L;
    public static final Long CAR_ID = 1L;

    public static final String username = "driver_username";
    public static final String password = "password";

    public static final String licensePlate = "car_plate";
    public static final Integer seatCount = 4;

    public static final double longitude = 1.1;
    public static final double latitude = 2.2;

    public static final double rating = 9.0;

    // DriverDOs:


    public static DriverDO aDriverDO()
    {
        return new DriverDO(username, password);
    }


    public static DriverDO aDeletedDriverDO()
    {
        DriverDO deleted = aDriverDO();
        deleted.setDeleted(true);
        return deleted;
    }


    public static DriverDO anOnlineDriverDO()
    {
        DriverDO online = aDriverDO();
        online.setOnlineStatus(OnlineStatus.ONLINE);
        return online;
    }


    public static DriverDO anOfflineDriverDO()
    {
        DriverDO online = aDriverDO();
        online.setOnlineStatus(OnlineStatus.OFFLINE);
        return online;
    }


    public static DriverDO anOfflineDriverWithCarDO()
    {
        DriverDO driver = anOfflineDriverDO();
        driver.setCar(aCarDO());
        return driver;
    }


    public static DriverDO aDriverWithCarDO()
    {
        DriverDO driver = aDriverDO();
        driver.setCar(aCarDO());
        return driver;
    }


    public static DriverDO aDriverWithCoordinateDO()
    {
        DriverDO driver = aDriverDO();
        GeoCoordinate coordinate = new GeoCoordinate(latitude, longitude);
        driver.setCoordinate(coordinate);
        return driver;
    }

    // DriverDTOs:


    public static DriverDTO aDriverDTO()
    {
        return DriverDTO.newBuilder()
            .setPassword(password)
            .setUsername(username).createDriverDTO();
    }


    public static DriverDTO aDriverWithCarDTO()
    {
        return DriverDTO.newBuilder()
            .setPassword(password)
            .setUsername(username)
            .setCar(aCarDTO())
            .createDriverDTO();
    }


    public static DriverDTO aDriverWithCoordinateDTO()
    {
        GeoCoordinate coordinate = new GeoCoordinate(latitude, longitude);
        return DriverDTO.newBuilder()
            .setPassword(password)
            .setUsername(username)
            .setCoordinate(coordinate).createDriverDTO();
    }


    public static DriverDTO anInvalidDriverDTO()
    {
        return DriverDTO.newBuilder().createDriverDTO();
    }

    //CarDOs:


    public static CarDO aCarDO()
    {
        return new CarDO(licensePlate, seatCount);
    }


    public static CarDO aDeletedCarDO()
    {
        CarDO deleted = aCarDO();
        deleted.setDeleted(true);
        return deleted;
    }


    public static CarDO aCarWithRatingDO()
    {
        CarDO car = aCarDO();
        car.setRating(rating);
        return car;
    }


    public static CarDO anElectricCarDO()
    {
        CarDO car = aCarDO();
        car.setEngineType(EngineType.ELECTRIC);
        return car;
    }

    //CarDTOs:


    public static CarDTO aCarDTO()
    {
        return CarDTO.newBuilder()
            .setLicensePlate(licensePlate)
            .setSeatCount(seatCount)
            .setRating(0.0)
            .setConvertible(false)
            .createCarDTO();
    }


    public static CarDTO aCarWithRatingDTO()
    {
        return CarDTO.newBuilder()
            .setLicensePlate(licensePlate)
            .setSeatCount(seatCount)
            .setRating(rating)
            .setConvertible(false)
            .createCarDTO();
    }


    public static CarDTO anElectricCarDTO()
    {
        return CarDTO.newBuilder()
            .setLicensePlate(licensePlate)
            .setSeatCount(seatCount)
            .setEngineType(String.valueOf(EngineType.ELECTRIC))
            .setRating(0.0)
            .setConvertible(false)
            .createCarDTO();
    }


    public static CarDTO anInvalidCarDTO()
    {
        String longLicensePlate = "longLicensePlateNumberMoreThan14Chars";
        Integer largeSeatCount = 10000;
        Double negativeRating = -1.0;
        String unknownEngineType = "IamUnknownEngineType";
        return CarDTO.newBuilder()
            .setLicensePlate(longLicensePlate)
            .setSeatCount(largeSeatCount)
            .setRating(negativeRating)
            .setEngineType(unknownEngineType).createCarDTO();

    }


    /**
     * Convert an object to Json string format.
     *
     * @param obj
     * @return
     */
    public static String asJsonString(final Object obj)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
