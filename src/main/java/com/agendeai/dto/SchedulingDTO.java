package com.agendeai.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SchedulingDTO {

    private Long id;
    private String clientName;
    private String barberName;
    private String typeServiceName;
    private LocalDateTime dateTime;
    private String status;

}
