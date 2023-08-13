package com.taxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxi.controller.validators.ValueOfEnum;
import com.taxi.domainvalue.EngineType;
import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO
{

    private final Long id;

    @Column(nullable = false)
    @NotBlank(message = "LicensePlate can not be null or empty!")
    @Size(min = 2, max = 14)
    private final String licensePlate;

    @Column(nullable = false)
    @NotNull(message = "seatCount can not be null!")
    @Min(value = 2, message = "Car should have at least 2 seats")
    @Max(value = 10, message = "Car should have at most 10 seats")
    private final Integer seatCount;

    @PositiveOrZero(message = "rating can not be negative value")
    private final Double rating;

    private final Boolean convertible;

    @ValueOfEnum(enumClass = EngineType.class, message = "Please provide valid engine type!")
    private final String engineType;

    private final ManufacturerDTO manufacturer;


    public CarDTO(Long id, String licensePlate, Integer seatCount, Double rating, Boolean convertible, String engineType, ManufacturerDTO manufacturer)
    {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.rating = rating;
        this.convertible = convertible;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }


    public static CarDTOBuilder newBuilder()
    {
        return new CarDTOBuilder();
    }


    @JsonProperty
    public Long getId()
    {
        return id;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public Double getRating()
    {
        return rating;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public String getEngineType()
    {
        return engineType;
    }


    public ManufacturerDTO getManufacturer()
    {
        return manufacturer;
    }


    public static class CarDTOBuilder
    {
        private Long id;
        private String licensePlate;
        private Integer seatCount;
        private Double rating;
        private Boolean convertible;
        private String engineType;
        private ManufacturerDTO manufacturer;


        public CarDTOBuilder setId(Long id)
        {
            this.id = id;
            return this;
        }


        public CarDTOBuilder setLicensePlate(String licensePlate)
        {
            this.licensePlate = licensePlate;
            return this;
        }


        public CarDTOBuilder setSeatCount(Integer seatCount)
        {
            this.seatCount = seatCount;
            return this;
        }


        public CarDTOBuilder setRating(Double rating)
        {
            this.rating = rating;
            return this;
        }


        public CarDTOBuilder setConvertible(Boolean convertible)
        {
            this.convertible = convertible;
            return this;
        }


        public CarDTOBuilder setEngineType(String engineType)
        {
            this.engineType = engineType;
            return this;
        }


        public CarDTOBuilder setManufacturer(ManufacturerDTO manufacturer)
        {
            this.manufacturer = manufacturer;
            return this;
        }


        public CarDTO createCarDTO()
        {
            return new CarDTO(id, licensePlate, seatCount, rating, convertible, engineType, manufacturer);
        }
    }
}
