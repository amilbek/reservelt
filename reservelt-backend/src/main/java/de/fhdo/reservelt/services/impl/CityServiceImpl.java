package de.fhdo.reservelt.services.impl;

import de.fhdo.reservelt.converters.CityMapper;
import de.fhdo.reservelt.domain.City;
import de.fhdo.reservelt.dto.CityDto;
import de.fhdo.reservelt.repositories.CityRepository;
import de.fhdo.reservelt.services.CityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    @Override
    public List<CityDto> findAllByCountry(Long countryId) {
        Iterator<City> iterator = cityRepository
                .findAllByCountryId(countryId)
                .iterator();
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterator, 0), false)
                .map(cityMapper::entityToDto).toList();
    }

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found with ID " + id));
    }
}
