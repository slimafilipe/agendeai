package com.agendeai.controller;

import com.agendeai.dto.SchedulingCreateDTO;
import com.agendeai.dto.SchedulingResponseDTO;
import com.agendeai.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService service;

    @PostMapping
    public ResponseEntity<SchedulingResponseDTO> create(@RequestBody SchedulingCreateDTO dto){
       return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/barbers/{barberId}")
    public ResponseEntity<List<SchedulingResponseDTO>> findByBarber(@PathVariable Long barberId){
        return ResponseEntity.ok(service.findByBarber(barberId));
    }

    @GetMapping("/clients/{clientId}")
        public ResponseEntity<List<SchedulingResponseDTO>> findByClient(@PathVariable Long clientId){
            return ResponseEntity.ok(service.findByClient(clientId));
        }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<SchedulingResponseDTO>> getDate(@PathVariable String date){
        return ResponseEntity.ok(service.findByDate(LocalDate.parse(date)));
    }

    @GetMapping
    public ResponseEntity<List<SchedulingResponseDTO>> listAll(){
        return ResponseEntity.ok(service.listAll());
    }
}
