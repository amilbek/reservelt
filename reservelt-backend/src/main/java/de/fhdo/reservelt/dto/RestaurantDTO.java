package de.fhdo.reservelt.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Data
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String rating;
    private List<FoodDTO> foods;

    // Constructors, Getters, Setters
    public RestaurantDTO(Long id, String name, String address, String phoneNumber, String rating, List<FoodDTO> foods) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.foods = foods;

    }

    public RestaurantDTO() {

    }

    // Getters and setters

}
