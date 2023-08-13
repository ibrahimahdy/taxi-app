package com.taxi.domainobject;

import com.taxi.domainvalue.EngineType;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(
    name = "car",
    uniqueConstraints = @UniqueConstraint(name = "uc_licensePlate", columnNames = {"licensePlate"})
)
public class CarDO
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(nullable = false)
    @NotBlank(message = "LicensePlate can not be null or empty!")
    @Size(min = 2, max = 14)
    private String licensePlate;

    @Column(nullable = false)
    @NotNull(message = "seatCount can not be null!")
    @Min(value = 2, message = "Car should have at least 2 seats")
    @Max(value = 10, message = "Car should have at most 10 seats")
    private Integer seatCount;

    @Column(nullable = false)
    private Boolean deleted = false;

    private Boolean convertible = false;

    @PositiveOrZero(message = "rating can not be negative value")
    @Max(value = 10, message = "rating can not be more than 10")
    private Double rating = 0.0;

    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    @ManyToOne
    private ManufacturerDO manufacturer;

    @OneToOne(mappedBy = "car")
    private DriverDO driver;


    public CarDO()
    {
    }


    public CarDO(String licensePlate, Integer seatCount)
    {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.deleted = false;
        this.convertible = false;
        this.rating = 0.0;
        this.engineType = null;
        this.manufacturer = null;
        this.driver = null;
    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    public ZonedDateTime getDateCreated()
    {
        return dateCreated;
    }


    public void setDateCreated(ZonedDateTime dateCreated)
    {
        this.dateCreated = dateCreated;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public void setSeatCount(Integer seatCount)
    {
        this.seatCount = seatCount;
    }


    public Boolean getDeleted()
    {
        return deleted;
    }


    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public void setConvertible(Boolean convertible)
    {
        this.convertible = convertible;
    }


    public Double getRating()
    {
        return rating;
    }


    public void setRating(Double rating)
    {
        this.rating = rating;
    }


    public EngineType getEngineType()
    {
        return engineType;
    }


    public void setEngineType(EngineType engineType)
    {
        this.engineType = engineType;
    }


    public ManufacturerDO getManufacturer()
    {
        return manufacturer;
    }


    public void setManufacturer(ManufacturerDO manufacturer)
    {
        this.manufacturer = manufacturer;
    }


    public DriverDO getDriver()
    {
        return driver;
    }


    public void setDriver(DriverDO driver)
    {
        this.driver = driver;
    }
}
