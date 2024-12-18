package de.fhdo.reservelt.restapi;

import de.fhdo.reservelt.dto.UserDto;
import de.fhdo.reservelt.dto.UserLoginDto;
import de.fhdo.reservelt.dto.UserRegisterDto;
import de.fhdo.reservelt.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;

    @Autowired
    public AuthRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        return userService.save(userRegisterDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginUser(@Valid @RequestBody UserLoginDto userRegisterDto) {
        return userService.authenticationToken(userRegisterDto);
    }
}
