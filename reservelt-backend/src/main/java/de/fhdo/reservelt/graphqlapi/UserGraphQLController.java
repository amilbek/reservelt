package de.fhdo.reservelt.graphqlapi;

import de.fhdo.reservelt.dto.ChangeUserPasswordDto;
import de.fhdo.reservelt.dto.UserDto;
import de.fhdo.reservelt.dto.UserEditDto;
import de.fhdo.reservelt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
public class UserGraphQLController {

    private final UserService userService;

    @Autowired
    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping("user")
    public UserDto getCurrentUser() {
        return userService.getProfile();
    }

    @MutationMapping("edit_user")
    public UserDto editUser(@Argument @Validated UserEditDto userEditDto) {
        return userService.editUserById(userEditDto);
    }

    @MutationMapping("delete_user")
    public String deleteUser() {
        userService.deleteCurrentUser();
        return "User deleted successfully";
    }

    @MutationMapping("change_password")
    public String changePassword(@Argument @Validated ChangeUserPasswordDto changeUserPasswordDto) {
        userService.changePassword(changeUserPasswordDto);
        return "Password changed successfully";
    }
}
