package de.fhdo.reservelt.converters;

import de.fhdo.reservelt.domain.City;
import de.fhdo.reservelt.dto.CityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    private final CountryMapper countryMapper;

    @Autowired
    public CityMapper(CountryMapper countryMapper) {
        this.countryMapper = countryMapper;
    }

    public CityDto entityToDto(City entity) {
        CityDto dto = new CityDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(countryMapper.entityToDto(entity.getCountry()));
        return dto;
    }

    public City dtoToEntity(CityDto dto) {
        City entity = new City();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCountry(countryMapper.dtoToEntity(dto.getCountry()));
        return entity;
    }
}
