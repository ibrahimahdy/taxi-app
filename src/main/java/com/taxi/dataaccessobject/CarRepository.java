package com.taxi.dataaccessobject;

import com.taxi.domainobject.CarDO;
import com.taxi.domainvalue.EngineType;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Data access object for car table.
 * <p/>
 */
public interface CarRepository extends CrudRepository<CarDO, Long>
{
    List<CarDO> findByEngineType(EngineType engineType);
}
