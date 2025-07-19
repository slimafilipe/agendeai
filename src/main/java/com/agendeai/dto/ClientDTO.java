package com.agendeai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDTO {
    private Long id;
    private String name;
    private String numberPhone;

}
