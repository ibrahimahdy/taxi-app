package com.taxi.controller.mapper;

import com.taxi.datatransferobject.ManufacturerDTO;
import com.taxi.domainobject.ManufacturerDO;

/**
 * Mapping domain object to data data access object and vice versa.
 * <p/>
 */
public final class ManufacturerMapper
{
    private ManufacturerMapper()
    {
    }


    public static ManufacturerDO makeManufacturerDO(ManufacturerDTO manufacturerDTO)
    {
        return new ManufacturerDO(manufacturerDTO.getId(), manufacturerDTO.getName());
    }


    public static ManufacturerDTO makeManufacturerDTO(ManufacturerDO manufacturerDO)
    {
        ManufacturerDTO.ManufacturerDTOBuilder manufacturerDTOBuilder =
            ManufacturerDTO.newBuilder()
                .setId(manufacturerDO.getId())
                .setName(manufacturerDO.getName());

        return manufacturerDTOBuilder.createManufacturerDTO();
    }
}
