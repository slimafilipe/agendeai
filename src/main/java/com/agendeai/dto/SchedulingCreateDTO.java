package com.agendeai.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchedulingCreateDTO {

    @NotNull
    private Long clientId;

    @NotNull
    private Long barberId;

    @NotNull
    private Long typeServicesId;

    @NotNull
    @Future(message = "A data e hora do agendamento devem está no futuro")
    private LocalDateTime dateTime;
}
