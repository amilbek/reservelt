package de.fhdo.reservelt.repositories;

import de.fhdo.reservelt.domain.RestaurantTableReservation;
import de.fhdo.reservelt.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantTableReservationRepository extends CrudRepository<RestaurantTableReservation, Long> {

    List<RestaurantTableReservation> findAllByUser(User user);
}
