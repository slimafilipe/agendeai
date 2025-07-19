package com.agendeai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceDTO {

    private Long id;
    private String nameService;
    private Double price;
    private Integer durationMinutes;
    private Long barberId;
    private String barberName;
}
