package de.fhdo.reservelt.restapi;

import de.fhdo.reservelt.dto.ChangeUserPasswordDto;
import de.fhdo.reservelt.dto.UserDto;
import de.fhdo.reservelt.dto.UserEditDto;
import de.fhdo.reservelt.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getProfile() {
        return userService.getProfile();
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public UserDto editUser(@Valid @RequestBody UserEditDto userEditDto) {
        return userService.editUserById(userEditDto);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser() {
        userService.deleteCurrentUser();
    }

    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void editUser(@Valid @RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        userService.changePassword(changeUserPasswordDto);
    }
}
