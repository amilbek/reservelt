package de.fhdo.reservelt.controllers;

import de.fhdo.reservelt.dto.RestaurantDTO;
import de.fhdo.reservelt.dto.SearchRequestDTO;
import de.fhdo.reservelt.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;


    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("searchRequest", new SearchRequestDTO());
        return "search";
    }

    @PostMapping("/search")
    public String searchRestaurants(SearchRequestDTO searchRequest, Model model) {
        List<RestaurantDTO> restaurants = restaurantService.searchRestaurants(searchRequest.getName());
        model.addAttribute("restaurants", restaurants);
        return "search";
    }
}
