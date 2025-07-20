package com.agendeai.service;

import com.agendeai.dto.SchedulingCreateDTO;
import com.agendeai.dto.SchedulingDTO;
import com.agendeai.dto.SchedulingResponseDTO;
import com.agendeai.model.Scheduling;
import com.agendeai.repository.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final SchedulingRepository schedulingRepository;

    private final ClientService clientService;
    private final BarberService barberService;
    private final TypeServicesService typeServicesService;


    public SchedulingResponseDTO create(SchedulingCreateDTO dto){
        var scheduling = new Scheduling();
        scheduling.setDateTime(dto.getDateTime());
        scheduling.setClient(clientService.findById(dto.getClientId()));
        scheduling.setBarber(barberService.findById(dto.getBarberId()));
        scheduling.setTypeServices(typeServicesService.findById(dto.getTypeServicesId()));


        Scheduling saved = schedulingRepository.save(scheduling);
        return toResponseDTO(saved);
    }



    //Faz busca por barbeiro
    public List<SchedulingResponseDTO> findByBarber(Long barberId){
        return schedulingRepository.findByBarberId(barberId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    //Faz busca por cliente
    public List<SchedulingResponseDTO> findByClient(Long clientId){
        return schedulingRepository.findByClientId(clientId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SchedulingResponseDTO> findByDate(LocalDate date){
        return schedulingRepository.findAll()
                .stream()
                .filter(s -> s.getDateTime().toLocalDate().equals(date))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private SchedulingResponseDTO toResponseDTO(Scheduling sch){
        SchedulingResponseDTO dto = new SchedulingResponseDTO();

        dto.setId(sch.getId());
        dto.setDateTime(sch.getDateTime());

        var client = sch.getClient();
        var barber = sch.getBarber();
        var service = sch.getTypeServices();

        dto.setClientId(client.getId());
        dto.setClientName(client.getName());
        dto.setBarberId(barber.getId());
        dto.setBarberName(barber.getName());
        dto.setServiceId(service.getId());
        dto.setServiceName(service.getNameService());

        return dto;
    }

    public List<SchedulingResponseDTO> listAll() {
        return schedulingRepository.findAll().stream().map(scheduling -> {SchedulingResponseDTO dto = new SchedulingResponseDTO();
        dto.setId(scheduling.getId());
        dto.setDateTime(scheduling.getDateTime());

        var client = scheduling.getClient();
        var barber = scheduling.getBarber();
        var service = scheduling.getTypeServices();

        dto.setClientId(client.getId());
        dto.setClientName(client.getName());
        dto.setBarberId(barber.getId());
        dto.setBarberName(barber.getName());
        dto.setServiceId(service.getId());
        dto.setServiceName(service.getNameService());

        return dto;

        }).toList();
    }


}
