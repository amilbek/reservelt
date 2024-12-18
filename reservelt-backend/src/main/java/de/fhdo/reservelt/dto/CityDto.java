package de.fhdo.reservelt.dto;

import lombok.Data;

@Data
public class CityDto {

    private Long id;
    private String name;
    private CountryDto country;
}
