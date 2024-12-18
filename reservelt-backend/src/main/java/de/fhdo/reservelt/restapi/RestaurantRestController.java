package de.fhdo.reservelt.restapi;

import de.fhdo.reservelt.domain.Restaurant;
import de.fhdo.reservelt.dto.RestaurantDTO;
import de.fhdo.reservelt.repositories.RestaurantRepository;
import de.fhdo.reservelt.services.RestaurantService;
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

    @Autowired
    public RestaurantRestController(RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.getAllRestaurantsWithFoods();
    }

    @GetMapping("/search")
    public List<RestaurantDTO> searchRestaurants(@RequestParam String name) {
        return restaurantService.searchRestaurants(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("id") Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

//    @PostMapping
//    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
//        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
//        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
//    }
}
