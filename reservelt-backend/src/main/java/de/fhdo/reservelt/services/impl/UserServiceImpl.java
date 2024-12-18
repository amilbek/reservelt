package de.fhdo.reservelt.services.impl;

import de.fhdo.reservelt.converters.UserMapper;
import de.fhdo.reservelt.domain.Role;
import de.fhdo.reservelt.domain.User;
import de.fhdo.reservelt.domain.enums.RoleName;
import de.fhdo.reservelt.dto.*;
import de.fhdo.reservelt.repositories.RoleRepository;
import de.fhdo.reservelt.repositories.UserRepository;
import de.fhdo.reservelt.security.JwtUtilities;
import de.fhdo.reservelt.services.CityService;
import de.fhdo.reservelt.services.CountryService;
import de.fhdo.reservelt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtUtilities jwtUtilities;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager ;
    private final CountryService countryService;
    private final CityService cityService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserMapper userMapper,
                           JwtUtilities jwtUtilities,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           CountryService countryService,
                           CityService cityService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.jwtUtilities = jwtUtilities;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.countryService = countryService;
        this.cityService = cityService;
    }


    @Override
    public UserDto save(UserRegisterDto userRegisterDto) {
        if (userRegisterDto.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getPasswordConfirmation())) {
            throw new IllegalArgumentException("Password and password confirmation do not match.");
        }

        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        User user = new User();
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setBirthDate(userRegisterDto.getBirthDate());
        user.setCountry(countryService.findById(userRegisterDto.getCountry()));
        user.setCity(cityService.findById(userRegisterDto.getCity()));
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        Role role = roleRepository.findByRoleName(RoleName.USER);
        user.setRoles(Collections.singletonList(role));
        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public UserDto editUserById(UserEditDto userEditDto) {
        if (userEditDto.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }

        User user = getCurrentUser();
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setBirthDate(userEditDto.getBirthDate());
        user.setCountry(countryService.findById(userEditDto.getCountry()));
        user.setCity(cityService.findById(userEditDto.getCity()));
        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public void deleteCurrentUser() {
        User user = getCurrentUser();
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public String authenticationToken(UserLoginDto userLoginDto) {
        if (!userRepository.existsByEmail(userLoginDto.getEmail())) {
            throw new BadCredentialsException("Bad credentials");
        }
        try {
            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            userLoginDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<String> rolesNames = new ArrayList<>();
            user.getRoles().forEach(r-> rolesNames.add(String.valueOf(r.getRoleName())));
            return jwtUtilities.generateToken(user.getUsername(), rolesNames);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public void changePassword(ChangeUserPasswordDto changeUserPasswordDto) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(changeUserPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match.");
        }
        if (!changeUserPasswordDto.getNewPassword().equals(changeUserPasswordDto.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("New password and password confirmation do not match.");
        }

        user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    private Object getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public UserDto getProfile() {
        return userMapper.entityToDto(getCurrentUser());
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(Objects.requireNonNull(getCurrentUserEmail()).toString()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
