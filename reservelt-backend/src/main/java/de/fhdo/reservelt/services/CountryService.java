package de.fhdo.reservelt.services;

import de.fhdo.reservelt.domain.Country;
import de.fhdo.reservelt.dto.CountryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {

    List<CountryDto> getAllCountries();

    Country findById(Long id);
}
