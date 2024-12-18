package de.fhdo.reservelt.controllers;

import de.fhdo.reservelt.dto.*;
import de.fhdo.reservelt.services.CityService;
import de.fhdo.reservelt.services.CountryService;
import de.fhdo.reservelt.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CountryService countryService;
    private final CityService cityService;

    @Autowired
    public UserController(UserService userService, CountryService countryService, CityService cityService) {
        this.userService = userService;
        this.countryService = countryService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getProfilePage(Model model) {
        UserDto user = userService.getProfile();
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/edit")
    public String getEditPage(Model model) {
        UserDto user = userService.getProfile();
        model.addAttribute("user", user);
        List<CountryDto> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);
        List<CityDto> cities = cityService.findAllByCountry(user.getCountry().getId());
        model.addAttribute("cities", cities);
        return "update";
    }

    @GetMapping("/change-password")
    public String getChangePasswordPage() {
        return "change-password";
    }

    @PostMapping("/edit")
    public String editUser(@Valid @ModelAttribute UserEditDto userEditDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("failed", "Validation errors occurred. Please correct the form and try again.");
            List<CountryDto> countries = countryService.getAllCountries();
            model.addAttribute("countries", countries);
            UserDto user = userService.getProfile();
            model.addAttribute("user", user);
            List<CityDto> cities = cityService.findAllByCountry(user.getCountry().getId());
            model.addAttribute("cities", cities);
            return "update";
        }

        try {
            userService.editUserById(userEditDto);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            List<CountryDto> countries = countryService.getAllCountries();
            model.addAttribute("countries", countries);
            UserDto user = userService.getProfile();
            model.addAttribute("user", user);
            List<CityDto> cities = cityService.findAllByCountry(user.getCountry().getId());
            model.addAttribute("cities", cities);
            model.addAttribute("failed", e.getMessage());
            return "update";
        } catch (Exception e) {
            List<CountryDto> countries = countryService.getAllCountries();
            model.addAttribute("countries", countries);
            UserDto user = userService.getProfile();
            model.addAttribute("user", user);
            List<CityDto> cities = cityService.findAllByCountry(user.getCountry().getId());
            model.addAttribute("cities", cities);
            model.addAttribute("failed", "An error occurred during editing user data. Please try again.");
            return "update";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute ChangeUserPasswordDto changeUserPasswordDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("failed", "Validation errors occurred. Please correct the form and try again.");
            return "change-password";
        }

        try {
            userService.changePassword(changeUserPasswordDto);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("failed", e.getMessage());
            return "change-password";
        } catch (Exception e) {
            model.addAttribute("failed", "An error occurred during changing password. Please try again.");
            return "change-password";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            userService.deleteCurrentUser();
            redirectAttributes.addFlashAttribute("success", "Your account has been deleted.");

            Cookie cookie = new Cookie("authToken", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while deleting your account.");
            return "redirect:/profile";
        }
    }
}
