package com.agendeai.controller;

import com.agendeai.dto.SchedulingCreateDTO;
import com.agendeai.dto.SchedulingDTO;
import com.agendeai.dto.SchedulingResponseDTO;
import com.agendeai.service.SchedulingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService service;

    @PostMapping
    public SchedulingDTO toSchedule(@RequestBody @Valid SchedulingCreateDTO dto){
        return service.toSchedule(dto);
    }

    @GetMapping("/barbers/{barberId}")
    public List<SchedulingDTO> findByBarber(@PathVariable Long barberId){
        return service.findByBarber(barberId);
    }

    @GetMapping("/clients/{clientId}")
        public List<SchedulingDTO> findByClient(@PathVariable Long clientId){
            return service.findByClient(clientId);
        }

    @GetMapping
    public ResponseEntity<List<SchedulingResponseDTO>> listAll(){
        return ResponseEntity.ok(service.listAll());
    }
}
