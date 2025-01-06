package de.fhdo.reservelt.dto;

import lombok.Data;

@Data
public class RestaurantTableReservationDto {

    private Long id;
    private String restaurantName;
    private String tableReservationStatus;
}
