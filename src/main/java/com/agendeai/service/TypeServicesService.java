package com.agendeai.service;

import com.agendeai.dto.ServiceCreateDTO;
import com.agendeai.model.TypeServices;
import com.agendeai.repository.TypeServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TypeServicesService {

    private final TypeServiceRepository typeServiceRepository;
    private final BarberService barberService;

    public TypeServices create (ServiceCreateDTO dto){
        var barber = barberService.findById(dto.getBarberId());

        var typeService = TypeServices.builder()
                .nameService(dto.getNameService())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .barber(barber)
                .build();
        return typeServiceRepository.save(typeService);
    }

    public List<TypeServices> findAll(){
        return typeServiceRepository.findAll();
    }

   public TypeServices findById(Long id){
       return typeServiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Serviço não encontrado com o ID " + id));
   }

   public List<TypeServices> findByBarber(Long barberId){
       return typeServiceRepository.findByBarberId(barberId);
   }



}
