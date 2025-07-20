package com.agendeai.controller;

import com.agendeai.dto.BarberCreateDTO;
import com.agendeai.dto.BarberDTO;
import com.agendeai.model.Barber;
import com.agendeai.repository.BarberRepository;
import com.agendeai.service.BarberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberRepository barberRepository;

    @PostMapping
    public ResponseEntity<Barber> register(@RequestBody  BarberCreateDTO dto){
        Barber barber = new Barber();
        barber.setName(dto.getName());
        barber.setEmail(dto.getEmail());
        barber.setNumberPhone(dto.getNumberPhone());
        barber.setPassword(dto.getPassword());

        return ResponseEntity.ok(barberRepository.save(barber));
    }

    @GetMapping
    public ResponseEntity<List<Barber>> list(){
        return ResponseEntity.ok(barberRepository.findAll());
    }

}
