package com.agendeai.service;

import com.agendeai.dto.SchedulingCreateDTO;
import com.agendeai.dto.SchedulingDTO;
import com.agendeai.dto.SchedulingResponseDTO;
import com.agendeai.model.Barber;
import com.agendeai.model.Client;
import com.agendeai.model.Scheduling;
import com.agendeai.model.TypeServices;
import com.agendeai.repository.BarberRepository;
import com.agendeai.repository.ClientRepository;
import com.agendeai.repository.SchedulingRepository;
import com.agendeai.repository.TypeServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final SchedulingRepository schedulingRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;
    private final TypeServiceRepository typeServiceRepository;

    public SchedulingDTO toSchedule(SchedulingCreateDTO dto){
        if(schedulingRepository.existsByBarberIdAndDateTime(dto.getBarberId(), dto.getDateTime())){
            throw new RuntimeException("Horário já ocupado para esse barbeiro");
        }

        Client client =clientRepository.findById(dto.getClientId()).orElseThrow(()-> new RuntimeException("Cliente não encontrado"));
        Barber barber = barberRepository.findById(dto.getBarberId()).orElseThrow(()-> new RuntimeException("Barbeiro não encontrado"));
        TypeServices services = typeServiceRepository.findById(dto.getTypeServicesId()).orElseThrow(()-> new RuntimeException("Serviço não encontrado"));

        Scheduling scheduling = Scheduling.builder()
                .client(client)
                .barber(barber)
                .typeServices(services)
                .dateTime(dto.getDateTime())
                .statusScheduling(Scheduling.StatusScheduling.AGENDADO)
                .build();
        return toDTO(schedulingRepository.save(scheduling));
    }

    //Faz busca por barbeiro
    public List<SchedulingDTO> findByBarber(Long barberId){
        return schedulingRepository.findByBarberId(barberId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    //Faz busca por cliente
    public List<SchedulingDTO> findByClient(Long clientId){
        return schedulingRepository.findByClientId(clientId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }


    private SchedulingDTO toDTO(Scheduling sch){
        return SchedulingDTO.builder()
                .id(sch.getId())
                .clientName(sch.getClient().getName())
                .barberName(sch.getBarber().getName())
                .typeServiceName(sch.getTypeServices().getNameService())
                .dateTime(sch.getDateTime())
                .status(sch.getStatusScheduling().name())
                .build();
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
