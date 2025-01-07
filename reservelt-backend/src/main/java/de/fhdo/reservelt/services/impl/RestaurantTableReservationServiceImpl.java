package de.fhdo.reservelt.services.impl;

import de.fhdo.reservelt.domain.Restaurant;
import de.fhdo.reservelt.domain.RestaurantTableReservation;
import de.fhdo.reservelt.domain.User;
import de.fhdo.reservelt.domain.enums.RestaurantTableReservationEnums;
import de.fhdo.reservelt.dto.RestaurantTableReservationDto;
import de.fhdo.reservelt.repositories.RestaurantRepository;
import de.fhdo.reservelt.repositories.RestaurantTableReservationRepository;
import de.fhdo.reservelt.services.RestaurantTableReservationService;
import de.fhdo.reservelt.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantTableReservationServiceImpl implements RestaurantTableReservationService {

    private final RestaurantTableReservationRepository restaurantTableReservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserService userService;

    @Autowired
    public RestaurantTableReservationServiceImpl(RestaurantTableReservationRepository restaurantTableReservationRepository, RestaurantRepository restaurantRepository, UserService userService) {
        this.restaurantTableReservationRepository = restaurantTableReservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.userService = userService;
    }

    @Override
    public void saveRestaurantTableReservation(Long restaurantId) {
        User user = userService.getCurrentUser();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with ID " + restaurantId));
        if (restaurant.getAvailableSeatCount() < 1) {
            throw new IllegalArgumentException("No available seat in " + restaurant.getName());
        }
        restaurant.setAvailableSeatCount(restaurant.getAvailableSeatCount() - 1);
        restaurantRepository.save(restaurant);
        RestaurantTableReservation restaurantTableReservation = new RestaurantTableReservation();
        restaurantTableReservation.setUser(user);
        restaurantTableReservation.setRestaurant(restaurant);
        restaurantTableReservation.setStatus(RestaurantTableReservationEnums.RESERVED);
        restaurantTableReservationRepository.save(restaurantTableReservation);
    }

    @Override
    public void updateRestaurantTableReservation(Long tableReservationId, RestaurantTableReservationEnums status) {
        RestaurantTableReservation restaurantTableReservation = restaurantTableReservationRepository.findById(tableReservationId)
                .orElseThrow(() -> new EntityNotFoundException("Table Reservation not found with ID " + tableReservationId));
        restaurantTableReservation.setStatus(status);
        restaurantTableReservationRepository.save(restaurantTableReservation);
        Restaurant restaurant = restaurantTableReservation.getRestaurant();
        if (!status.equals(RestaurantTableReservationEnums.IN_PROCESS)) {
            restaurant.setAvailableSeatCount(restaurant.getAvailableSeatCount() + 1);
        }
        restaurantRepository.save(restaurant);
    }

    @Override
    public List<RestaurantTableReservationDto> restaurantTableReservationsByUser() {
        User user = userService.getCurrentUser();
        return restaurantTableReservationRepository.findAllByUser(user)
                .stream()
                .map(x -> {
                    RestaurantTableReservationDto dto = new RestaurantTableReservationDto();
                    dto.setId(x.getId());
                    dto.setRestaurantName(x.getRestaurant().getName());
                    dto.setTableReservationStatus(x.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
