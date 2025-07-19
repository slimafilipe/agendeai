package com.agendeai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "barbers")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String numberPhone;

    @Column(nullable = false)
    private String password;



}
