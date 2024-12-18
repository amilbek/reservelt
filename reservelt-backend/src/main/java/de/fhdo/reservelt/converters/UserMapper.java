package de.fhdo.reservelt.converters;

import de.fhdo.reservelt.domain.User;
import de.fhdo.reservelt.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;

    public UserMapper(CountryMapper countryMapper, CityMapper cityMapper) {
        this.countryMapper = countryMapper;
        this.cityMapper = cityMapper;
    }


    public UserDto entityToDto(User entity) {
        if (entity == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setBirthDate(entity.getBirthDate());
        dto.setCountry(countryMapper.entityToDto(entity.getCountry()));
        dto.setCity(cityMapper.entityToDto(entity.getCity()));
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public User dtoToEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        user.setCountry(countryMapper.dtoToEntity(dto.getCountry()));
        user.setCity(cityMapper.dtoToEntity(dto.getCity()));
        user.setEmail(dto.getEmail());
        return user;
    }
}
