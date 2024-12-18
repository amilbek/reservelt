package de.fhdo.reservelt.graphqlapi;

import de.fhdo.reservelt.dto.UserDto;
import de.fhdo.reservelt.dto.UserLoginDto;
import de.fhdo.reservelt.dto.UserRegisterDto;
import de.fhdo.reservelt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
public class AuthGraphQLController {

    private final UserService userService;

    @Autowired
    public AuthGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping("register_user")
    public UserDto registerUser(@Argument @Validated UserRegisterDto userRegisterDto) {
        return userService.save(userRegisterDto);
    }

    @MutationMapping("login_user")
    public String loginUser(@Argument UserLoginDto userLoginDto) {
        return userService.authenticationToken(userLoginDto);
    }
}
