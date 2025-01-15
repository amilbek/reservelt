package de.fhdo.reservelt.restapi;

import de.fhdo.reservelt.domain.Restaurant;
import de.fhdo.reservelt.domain.enums.RestaurantTableReservationEnums;
import de.fhdo.reservelt.dto.RestaurantDTO;
import de.fhdo.reservelt.dto.RestaurantTableReservationDto;
import de.fhdo.reservelt.repositories.RestaurantRepository;
import de.fhdo.reservelt.services.RestaurantService;
import de.fhdo.reservelt.services.RestaurantTableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantRestController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private final RestaurantTableReservationService restaurantTableReservationService;


    @Autowired
    public RestaurantRestController(RestaurantRepository restaurantRepository, RestaurantService restaurantService, RestaurantTableReservationService restaurantTableReservationService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
        this.restaurantTableReservationService = restaurantTableReservationService;
    }

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.getAllRestaurantsWithFoods();
    }

    @GetMapping("/search")
    public List<RestaurantDTO> searchRestaurants(@RequestParam String name) {
        return restaurantService.searchRestaurants(name, name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("id") Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/save-table-reservation/{id}")
    public ResponseEntity<String> saveTableReservation(@PathVariable("id") Long id) {
        restaurantTableReservationService.saveRestaurantTableReservation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<List<RestaurantTableReservationDto>> getUserReservations() {
        List<RestaurantTableReservationDto> reservations = restaurantTableReservationService.restaurantTableReservationsByUser();
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/change-status")
    public ResponseEntity<String> changeReservationStatus(@RequestParam("reservationId") Long reservationId,
                                                          @RequestParam("newStatus") RestaurantTableReservationEnums newStatus) {
        restaurantTableReservationService.updateRestaurantTableReservation(reservationId, newStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
//        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
//        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
//    }
}
