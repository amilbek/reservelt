package de.fhdo.reservelt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String redirectToSearch() {
        return "redirect:/restaurants/search";
    }
}

