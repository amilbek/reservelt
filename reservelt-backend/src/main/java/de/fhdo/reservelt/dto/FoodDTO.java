package de.fhdo.reservelt.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Data
public class FoodDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    // Constructors,
    public FoodDTO(Long id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public FoodDTO() {

    }


    @Override
    public String toString() {
        return "FoodDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
