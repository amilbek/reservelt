package de.fhdo.reservelt.bootstrap;

import de.fhdo.reservelt.domain.*;
import de.fhdo.reservelt.domain.enums.RoleName;
import de.fhdo.reservelt.dto.UserRegisterDto;
import de.fhdo.reservelt.repositories.*;
import de.fhdo.reservelt.services.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DummyDataBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    public DummyDataBootstrap(UserService userService,
                              RoleRepository roleRepository,
                              CountryRepository countryRepository,
                              CityRepository cityRepository, RestaurantRepository restaurantRepository, FoodRepository foodRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
        Country country1 = new Country();
        country1.setId(1L);
        country1.setName("Germany");
        countryRepository.save(country1);

        Country country2 = new Country();
        country2.setId(2L);
        country2.setName("Kazakhstan");
        countryRepository.save(country2);

        Country country3 = new Country();
        country3.setId(3L);
        country3.setName("France");
        countryRepository.save(country3);

        City city1 = new City();
        city1.setId(1L);
        city1.setName("Dortmund");
        city1.setCountry(country1);
        cityRepository.save(city1);

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Taraz");
        city2.setCountry(country2);
        cityRepository.save(city2);

        City city3 = new City();
        city3.setId(3L);
        city3.setName("Lyon");
        city3.setCountry(country3);
        cityRepository.save(city3);

        City city4 = new City();
        city4.setId(4L);
        city4.setName("Berlin");
        city4.setCountry(country1);
        cityRepository.save(city4);

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName(RoleName.USER);
        roleRepository.save(userRole);

        Role restaurantRole = new Role();
        restaurantRole.setId(2L);
        restaurantRole.setRoleName(RoleName.RESTAURANT);
        roleRepository.save(restaurantRole);

        Role adminRole = new Role();
        adminRole.setId(3L);
        adminRole.setRoleName(RoleName.ADMIN);
        roleRepository.save(adminRole);

        Restaurant resGrammons = new Restaurant();
        resGrammons.setId(1L);
        resGrammons.setName("Grammons Restaurant");
        resGrammons.setAddress("Wieckesweg 29, 44309 Dortmund");
        resGrammons.setPhoneNumber("+49 231 93144465");
        resGrammons.setRating("4.6");
        restaurantRepository.save(resGrammons);

        Restaurant resTheStage = new Restaurant();
        resTheStage.setId(2L);
        resTheStage.setName("The Stage");
        resTheStage.setAddress("Dula Center, Nortkirchenstraße 53, 44263 Dortmund");
        resTheStage.setPhoneNumber("+49 231 2222550");
        resTheStage.setRating("4.7");

        restaurantRepository.save(resTheStage);

        Food GrammonsFood1 = new Food();
        GrammonsFood1.setId(1L);
        GrammonsFood1.setName("Hamburger");
        GrammonsFood1.setDescription("Hamburger mit Butter und Salat");
        GrammonsFood1.setPrice(BigDecimal.valueOf(8.2));
        GrammonsFood1.setRestaurant(resGrammons);

        foodRepository.save(GrammonsFood1);
        Food GrammonsFood2 = new Food();
        GrammonsFood2.setId(2L);
        GrammonsFood2.setName("Pizza");
        GrammonsFood2.setDescription("Pizza mit Tomaten und Paprika");
        GrammonsFood2.setPrice(BigDecimal.valueOf(10.5));
        GrammonsFood2.setRestaurant(resGrammons);

        foodRepository.save(GrammonsFood2);

        Food TheStageFood1 = new Food();
        TheStageFood1.setId(3L);
        TheStageFood1.setName("Burger");
        TheStageFood1.setDescription("Burger mit Butter und Kasse");
        TheStageFood1.setPrice(BigDecimal.valueOf(7.5));
        TheStageFood1.setRestaurant(resTheStage);

        foodRepository.save(TheStageFood1);
        Food TheStageFood2 = new Food();
        TheStageFood2.setId(4L);
        TheStageFood2.setName("Pizza suchi");
        TheStageFood2.setDescription("Pizza mit Tomaten und Paprika");
        TheStageFood2.setPrice(BigDecimal.valueOf(10.5));
        TheStageFood2.setRestaurant(resTheStage);

        foodRepository.save(TheStageFood2);

        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setFirstName("John");
        userRegisterDto.setLastName("Doe");
        userRegisterDto.setBirthDate(LocalDate.of(1999, 12, 31));
        userRegisterDto.setCountry(1L);
        userRegisterDto.setCity(1L);
        userRegisterDto.setEmail("email@mail.com");
        userRegisterDto.setPassword("Qwerty123!");
        userRegisterDto.setPasswordConfirmation("Qwerty123!");
        userService.save(userRegisterDto);
    }
}
