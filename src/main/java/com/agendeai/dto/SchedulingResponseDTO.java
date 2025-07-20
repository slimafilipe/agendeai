package com.agendeai.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class SchedulingResponseDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long barberId;
    private String barberName;
    private Long serviceId;
    private String serviceName;
    private LocalDateTime dateTime;
}
