package de.fhdo.reservelt.services;

import de.fhdo.reservelt.domain.enums.RestaurantTableReservationEnums;
import de.fhdo.reservelt.dto.RestaurantTableReservationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantTableReservationService {

    void saveRestaurantTableReservation(Long restaurantId);

    void updateRestaurantTableReservation(Long tableReservationId, RestaurantTableReservationEnums status);

    List<RestaurantTableReservationDto> restaurantTableReservationsByUser();
}
