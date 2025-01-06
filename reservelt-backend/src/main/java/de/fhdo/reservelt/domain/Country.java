package de.fhdo.reservelt.domain;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "countries")
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
