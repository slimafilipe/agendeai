package com.agendeai.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceCreateDTO {

    @NotBlank
    private String nameService;

    @NotNull
    @Min(1)
    private Double price;

    @NotNull
    @Min(1)
    private Integer durationMinutes;

    @NotNull
    private Long barberId;

}
