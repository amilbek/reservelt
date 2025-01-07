package de.fhdo.reservelt.controllers;

import de.fhdo.reservelt.domain.enums.RestaurantTableReservationEnums;
import de.fhdo.reservelt.dto.RestaurantDTO;
import de.fhdo.reservelt.dto.RestaurantTableReservationDto;
import de.fhdo.reservelt.dto.SearchRequestDTO;
import de.fhdo.reservelt.services.RestaurantService;
import de.fhdo.reservelt.services.RestaurantTableReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantTableReservationService restaurantTableReservationService;


    @Autowired
    public RestaurantController(RestaurantService restaurantService, RestaurantTableReservationService restaurantTableReservationService) {
        this.restaurantService = restaurantService;
        this.restaurantTableReservationService = restaurantTableReservationService;
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("searchRequest", new SearchRequestDTO());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        model.addAttribute("isAuthenticated", isAuthenticated);

        return "search";
    }

    @PostMapping("/search")
    public String searchRestaurants(SearchRequestDTO searchRequest, Model model) {
        List<RestaurantDTO> restaurants = restaurantService.searchRestaurants(searchRequest.getName());
        model.addAttribute("restaurants", restaurants);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        model.addAttribute("isAuthenticated", isAuthenticated);

        return "search";
    }

    @PostMapping("/save-table-reservation/{id}")
    public String saveTableReservation(@PathVariable("id") Long id, Model model) {
        try {
            restaurantTableReservationService.saveRestaurantTableReservation(id);
            model.addAttribute("message", "Reservation requested successfully!");
            model.addAttribute("messageType", "success");
        } catch (EntityNotFoundException e) {
            model.addAttribute("message", "Restaurant not found");
            model.addAttribute("messageType", "error");
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", "No available seat");
            model.addAttribute("messageType", "error");
        } catch (Exception e) {
            model.addAttribute("message", "An unexpected error occurred. Please try again later.");
            model.addAttribute("messageType", "error");
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        model.addAttribute("isAuthenticated", isAuthenticated);

        return "search";
    }

    @GetMapping("/my-reservations")
    public String getUserReservations(Model model) {
        List<RestaurantTableReservationDto> reservations = restaurantTableReservationService.restaurantTableReservationsByUser();

        model.addAttribute("reservations", reservations);
        return "user-reservations";
    }

    @PostMapping("/change-status")
    public String changeReservationStatus(@RequestParam("reservationId") Long reservationId,
                                          @RequestParam("newStatus") RestaurantTableReservationEnums newStatus,
                                          RedirectAttributes redirectAttributes) {
        try {
            restaurantTableReservationService.updateRestaurantTableReservation(reservationId, newStatus);
            redirectAttributes.addFlashAttribute("message", "Reservation status updated successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to update reservation status.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/restaurants/my-reservations";
    }
}
