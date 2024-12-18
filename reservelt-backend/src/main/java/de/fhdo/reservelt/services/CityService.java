package de.fhdo.reservelt.services;

import de.fhdo.reservelt.domain.City;
import de.fhdo.reservelt.dto.CityDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CityService {

    List<CityDto> findAllByCountry(Long countryId);

    City findById(Long id);
}
