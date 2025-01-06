package de.fhdo.reservelt.domain;

import de.fhdo.reservelt.domain.enums.RestaurantTableReservationEnums;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "restaurant_table_reservations")
@Data
public class RestaurantTableReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Restaurant restaurant;

    private RestaurantTableReservationEnums status;
}
