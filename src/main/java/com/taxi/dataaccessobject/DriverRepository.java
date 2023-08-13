package com.taxi.dataaccessobject;

import com.taxi.domainobject.CarDO;
import com.taxi.domainobject.DriverDO;
import com.taxi.domainvalue.OnlineStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>, JpaSpecificationExecutor<DriverDO>
{

    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);

    DriverDO findFirstByCar(CarDO car);
}
