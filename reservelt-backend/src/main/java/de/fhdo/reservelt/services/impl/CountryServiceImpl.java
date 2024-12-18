package de.fhdo.reservelt.services.impl;

import de.fhdo.reservelt.converters.CountryMapper;
import de.fhdo.reservelt.domain.Country;
import de.fhdo.reservelt.dto.CountryDto;
import de.fhdo.reservelt.repositories.CountryRepository;
import de.fhdo.reservelt.services.CountryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    @Override
    public List<CountryDto> getAllCountries() {
        Iterator<Country> iterator = countryRepository
                .findAll()
                .iterator();
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterator, 0), false)
                .map(countryMapper::entityToDto).toList();
    }

    @Override
    public Country findById(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with ID " + id));
    }
}
