package com.agendeai.controller;

import com.agendeai.dto.BarberCreateDTO;
import com.agendeai.dto.BarberDTO;
import com.agendeai.service.BarberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;

    @PostMapping
    public BarberDTO register(@RequestBody @Valid BarberCreateDTO dto){
        return barberService.register(dto);
    }

    @GetMapping
    public List<BarberDTO> list(){
        return barberService.listAll();
    }

}
