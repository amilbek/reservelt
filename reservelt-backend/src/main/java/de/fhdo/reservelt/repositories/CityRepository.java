package de.fhdo.reservelt.repositories;

import de.fhdo.reservelt.domain.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    List<City> findAllByCountryId(Long id);
}
