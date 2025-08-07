package com.agendeai.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduling")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scheduling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Barber barber;

    @ManyToOne
    private TypeServices typeServices;

    @Column(nullable = false)
    private LocalDateTime dateTime;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusScheduling statusScheduling;

    public enum StatusScheduling{
        AGENDADO,
        CANCELADO,
        FINALIZADO
    }
}
