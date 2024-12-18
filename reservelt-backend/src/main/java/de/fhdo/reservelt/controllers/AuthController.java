package de.fhdo.reservelt.controllers;

import de.fhdo.reservelt.dto.*;
import de.fhdo.reservelt.services.CityService;
import de.fhdo.reservelt.services.CountryService;
import de.fhdo.reservelt.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final CountryService countryService;
    private final CityService cityService;

    @Autowired
    public AuthController(UserService userService, CountryService countryService, CityService cityService) {
        this.userService = userService;
        this.countryService = countryService;
        this.cityService = cityService;
    }

    @GetMapping("/register")
    public String userRegisterPage(Model model) {
        List<CountryDto> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);
        return "register";
    }

    @GetMapping("/login")
    public String userLoginPage() {
        return "login";
    }

    @GetMapping("/country-list")
    @ResponseBody
    public List<CountryDto> getCountryList() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{countryId}/city-list")
    @ResponseBody
    public List<CityDto> getCityList(@PathVariable Long countryId) {
        return cityService.findAllByCountry(countryId);
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegisterDto userRegisterDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();

            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            model.addAttribute("failed", errorMessages.get(0));
            List<CountryDto> countries = countryService.getAllCountries();
            model.addAttribute("countries", countries);
            return "register";
        }

        try {
            userService.save(userRegisterDto);
            model.addAttribute("succeed", "Registration successful! You can now log in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("failed", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("failed", "An error occurred during registration. Please try again.");
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserLoginDto userLoginDto,
                        BindingResult bindingResult,
                        HttpServletResponse response,
                        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("failed", "Invalid username or password.");
            return "login";
        }

        try {
            String token = userService.authenticationToken(userLoginDto);

            Cookie authCookie = new Cookie("authToken", token);
            authCookie.setHttpOnly(true);
            authCookie.setSecure(true);
            authCookie.setPath("/");
            response.addCookie(authCookie);

            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("failed", "Bad Credentials");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        Cookie cookie = new Cookie("authToken", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/auth/login";
    }
}
