package com.agendeai.service;

import com.agendeai.dto.ServiceCreateDTO;
import com.agendeai.dto.ServiceDTO;
import com.agendeai.model.Barber;
import com.agendeai.model.TypeServices;
import com.agendeai.repository.BarberRepository;
import com.agendeai.repository.TypeServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeServicesService {

    private TypeServiceRepository typeServiceRepository;
    private BarberRepository barberRepository;

    public ServiceDTO register(ServiceCreateDTO dto){
        Barber barber = barberRepository.findById(dto.getBarberId())
                .orElseThrow(()-> new RuntimeException("Barbeiro não encontrado"));

        TypeServices typeServices = TypeServices.builder()
                .nameService(dto.getNameService())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .barber(barber)
                .build();
        return toDto(typeServiceRepository.save(typeServices));
    }

    public List<ServiceDTO> listByBarber(Long barberId){
        return typeServiceRepository.findByBarberId(barberId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ServiceDTO toDto(TypeServices typeServices){
        return ServiceDTO.builder()
                .id(typeServices.getId())
                .nameService(typeServices.getNameService())
                .price(typeServices.getPrice())
                .durationMinutes(typeServices.getDurationMinutes())
                .barberId(typeServices.getBarber().getId())
                .barberName(typeServices.getBarber().getName())
                .build();
    }


}
