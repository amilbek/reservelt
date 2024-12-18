package de.fhdo.reservelt.repositories;


import de.fhdo.reservelt.domain.Food;
import de.fhdo.reservelt.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
        List<Food> findAllByRestaurantId(Long restaurantId);
}
