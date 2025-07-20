package com.agendeai.service;

import com.agendeai.dto.BarberCreateDTO;
import com.agendeai.dto.BarberDTO;
import com.agendeai.model.Barber;
import com.agendeai.repository.BarberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;



    public Barber findById(Long id){
        return barberRepository.findById(id).orElseThrow(() -> new RuntimeException("Barbeiro não encontrado com o ID " + id));
    }


    /*
    public BarberDTO register(@Valid BarberCreateDTO dto){
        Barber barber = Barber.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .numberPhone(dto.getNumberPhone())
                .password(dto.getPassword())
                .build();
        Barber save = barberRepository.save(barber);
        return toDTO(save);
    }

    public List<BarberDTO> listAll(){
        return barberRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BarberDTO toDTO(Barber barber) {
        return BarberDTO.builder()
                .id(barber.getId())
                .name(barber.getName())
                .email(barber.getEmail())
                .numberPhone(barber.getNumberPhone())
                .password(barber.getPassword())
                .build();
    }

     */
}
