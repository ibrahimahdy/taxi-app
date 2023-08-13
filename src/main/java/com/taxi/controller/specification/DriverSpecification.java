package com.taxi.controller.specification;

import com.taxi.domainobject.CarDO;
import com.taxi.domainobject.CarDO_;
import com.taxi.domainobject.DriverDO;
import com.taxi.domainobject.DriverDO_;
import com.taxi.domainvalue.EngineType;
import com.taxi.domainvalue.OnlineStatus;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification for searching drivers.
 * <p/>
 */
public class DriverSpecification implements Specification<DriverDO>
{
    private String username;
    private OnlineStatus onlineStatus;
    private String licensePlate;
    private Integer seatCount;
    private Boolean convertible;
    private Double rating;
    private EngineType engineType;


    @Override
    public Predicate toPredicate(Root<DriverDO> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Join<DriverDO, CarDO> carJoined = root.join("car", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if (username != null)
        {
            predicates.add(builder.like(root.get(DriverDO_.username), "%" + username + "%"));
        }
        if (onlineStatus != null)
        {
            predicates.add(builder.equal(root.get(DriverDO_.onlineStatus), onlineStatus));
        }

        if (licensePlate != null)
        {
            predicates.add(builder.like(carJoined.get(CarDO_.licensePlate), "%" + licensePlate + "%"));
        }
        if (seatCount != null)
        {
            predicates.add(builder.equal(carJoined.get(CarDO_.seatCount), seatCount));
        }
        if (convertible != null)
        {
            predicates.add(builder.equal(carJoined.get(CarDO_.convertible), convertible));
        }
        if (rating != null)
        {
            predicates.add(builder.greaterThanOrEqualTo(carJoined.get(CarDO_.rating), rating));
        }
        if (engineType != null)
        {
            predicates.add(builder.equal(carJoined.get(CarDO_.engineType), engineType));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }


    public String getUsername()
    {
        return username;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }


    public OnlineStatus getOnlineStatus()
    {
        return onlineStatus;
    }


    public void setOnlineStatus(OnlineStatus onlineStatus)
    {
        this.onlineStatus = onlineStatus;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public void setSeatCount(Integer seatCount)
    {
        this.seatCount = seatCount;
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
}