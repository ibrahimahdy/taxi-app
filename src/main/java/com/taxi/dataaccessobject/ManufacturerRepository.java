package com.taxi.dataaccessobject;

import com.taxi.domainobject.ManufacturerDO;
import org.springframework.data.repository.CrudRepository;

/**
 * Data access object for manufacturer table.
 * <p/>
 */
public interface ManufacturerRepository extends CrudRepository<ManufacturerDO, Long>
{
}
