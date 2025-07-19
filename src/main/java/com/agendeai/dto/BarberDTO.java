package com.agendeai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BarberDTO {

    private Long id;
    private String name;
    private String email;
    private String numberPhone;
    private String password;
}
