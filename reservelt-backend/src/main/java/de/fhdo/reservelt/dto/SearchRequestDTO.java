package de.fhdo.reservelt.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchRequestDTO {

    // Getter and Setter
    private String name;

    // Constructors
    public SearchRequestDTO() {}

    public SearchRequestDTO(String name) {
        this.name = name;
    }

    public String getFoodName() {
        return name;
    }
}
