package de.fhdo.reservelt.services;

import de.fhdo.reservelt.domain.User;
import de.fhdo.reservelt.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto save(UserRegisterDto userRegisterDto);

    UserDto editUserById(UserEditDto userEditDto);

    void deleteCurrentUser();

    String authenticationToken(UserLoginDto userLoginDto);

    void changePassword(ChangeUserPasswordDto changeUserPasswordDto);

    UserDto getProfile();

    User getCurrentUser();
}
