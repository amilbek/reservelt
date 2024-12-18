package de.fhdo.reservelt.graphqlapi;

import de.fhdo.reservelt.dto.FoodDTO;
import de.fhdo.reservelt.dto.RestaurantDTO;

import de.fhdo.reservelt.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RestaurantGraphQLController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantGraphQLController( RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @QueryMapping
    public List<RestaurantDTO> restaurants() {
        return restaurantService.getAllRestaurantsWithFoods();
    }

    @QueryMapping
    public RestaurantDTO restaurantById(@Argument Long id) {
        return restaurantService.getAllRestaurantsWithFoods()
                .stream()
                .filter(restaurant -> restaurant.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    @QueryMapping
    public List<FoodDTO> foodsByRestaurant(@Argument Long restaurantId) {
        return restaurantService.getFoodsByRestaurantId(restaurantId);
    }

}
