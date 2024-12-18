package de.fhdo.reservelt.services;

import de.fhdo.reservelt.domain.Food;
import de.fhdo.reservelt.domain.Restaurant;
import de.fhdo.reservelt.dto.FoodDTO;
import de.fhdo.reservelt.dto.RestaurantDTO;
import de.fhdo.reservelt.repositories.FoodRepository;
import de.fhdo.reservelt.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, FoodRepository foodRepository) {
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
    }

    public List<RestaurantDTO> getAllRestaurantsWithFoods() {
        return restaurantRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FoodDTO> getFoodsByRestaurantId(Long restaurantId) {
        return foodRepository.findAllByRestaurantId(restaurantId).stream()
                .map(this::convertToFoodDTO)
                .collect(Collectors.toList());
    }

//    public List<RestaurantDTO> searchRestaurants(String name) {
//        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(name);
//        return restaurants.stream()
//                .map(this::convertToDTO) // Reuse the existing convertToDTO method
//                .collect(Collectors.toList());
//    }

    public List<RestaurantDTO> searchRestaurants(String name) {
        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(name);
        return restaurants.stream()
                .map(r -> {
                    List<FoodDTO> foods = foodRepository.findAllByRestaurantId(r.getId())
                            .stream()
                            .map(f -> new FoodDTO(f.getId(), f.getName(), f.getDescription(), f.getPrice()))
                            .collect(Collectors.toList());
                    return new RestaurantDTO(r.getId(), r.getName(), r.getAddress(), r.getPhoneNumber(), r.getRating(), foods);
                })
                .collect(Collectors.toList());
    }


    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setAddress(restaurant.getAddress());
        dto.setPhoneNumber(restaurant.getPhoneNumber());
        dto.setRating(restaurant.getRating());

        // Ensure foods are fetched if not already loaded
        List<FoodDTO> foods = restaurant.getFoods() != null && !restaurant.getFoods().isEmpty()
                ? restaurant.getFoods().stream()
                .map(this::convertToFoodDTO)
                .collect(Collectors.toList())
                : foodRepository.findAllByRestaurantId(restaurant.getId())
                .stream()
                .map(this::convertToFoodDTO)
                .collect(Collectors.toList());

        dto.setFoods(foods);
        return dto;
    }

    private FoodDTO convertToFoodDTO(Food food) {
        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setDescription(food.getDescription());
        dto.setPrice(food.getPrice());
        return dto;
    }


//    public List<RestaurantDTO> searchRestaurants(String name) {
//        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(name);
//        return restaurants.stream()
//                .map(r -> {
//                    List<FoodDTO> foods = foodRepository.findAllByRestaurantId(r.getId())
//                            .stream()
//                            .map(f ->  new FoodDTO(f.getId(), f.getName(), f.getDescription(), f.getPrice()))
//                            .collect(Collectors.toList());
//                    return new RestaurantDTO(r.getId(), r.getName(), r.getAddress(), r.getPhoneNumber(),r.getRating(), foods);
//                })
//                .collect(Collectors.toList());
//    }
}


//@Service
//public class RestaurantService {
//    private final RestaurantRepository restaurantRepository;
//
//    @Autowired
//    public RestaurantService(RestaurantRepository restaurantRepository) {
//        this.restaurantRepository = restaurantRepository;
//    }
//
//    public List<RestaurantDTO> searchRestaurants(String name) {
//        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(name);
//        return restaurants.stream()
//                .map(r -> new RestaurantDTO(r.getId(), r.getName(), r.getAddress(), r.getPhoneNumber(),r.getRating()))
//                .collect(Collectors.toList());
//    }
//}
