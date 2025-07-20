package com.agendeai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String numberPhone;

    @NotBlank
    private String password;
}
