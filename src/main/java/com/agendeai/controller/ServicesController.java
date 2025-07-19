package com.agendeai.controller;

import com.agendeai.dto.ServiceCreateDTO;
import com.agendeai.dto.ServiceDTO;
import com.agendeai.service.TypeServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    private final TypeServicesService typeServicesService;

    @PostMapping
    public ServiceDTO register(@RequestBody @Valid ServiceCreateDTO dto){
        return typeServicesService.register(dto);
    }

    @GetMapping("/barbers/{barberId}")
    public List<ServiceDTO> listByBarber(@PathVariable Long barberId){
        return typeServicesService.listByBarber(barberId);
    }
}
