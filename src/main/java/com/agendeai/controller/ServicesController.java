package com.agendeai.controller;

import com.agendeai.dto.ServiceCreateDTO;
import com.agendeai.model.TypeServices;
import com.agendeai.repository.TypeServiceRepository;
import com.agendeai.service.TypeServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServicesController {

    private final TypeServiceRepository typeServiceRepository;

    private final TypeServicesService services;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ServiceCreateDTO dto){
        if (typeServiceRepository.existsByNameServiceAndBarberId(dto.getNameService(), dto.getBarberId())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Esse barbeiro já possui esse serviço cadastrado.");
        }

        TypeServices saved = services.create(dto);


        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /*
    @PostMapping
    public ResponseEntity<TypeServices> register(@RequestBody  ServiceCreateDTO dto){
        TypeServices services = new TypeServices();
        services.setNameService(dto.getNameService());
        services.setPrice(dto.getPrice());
        services.setDurationMinutes(dto.getDurationMinutes());
        services.setBarber(dto.getBarberId());

        return ResponseEntity.ok(typeServiceRepository.save(services));
    }

     */

    @GetMapping()
    public List<TypeServices> listAll(){
        return services.findAll();
    }

    @GetMapping("/barbers/{barberId}")
    public List<TypeServices> listByBarber(@PathVariable Long barberId){
        return services.findByBarber(barberId);
    }
}
