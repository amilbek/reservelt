package de.fhdo.reservelt.converters;

import de.fhdo.reservelt.domain.Country;
import de.fhdo.reservelt.dto.CountryDto;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    public CountryDto entityToDto(Country entity) {
        CountryDto dto = new CountryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public Country dtoToEntity(CountryDto dto) {
        Country entity = new Country();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
