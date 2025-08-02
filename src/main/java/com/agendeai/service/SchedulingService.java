package com.agendeai.service;

import com.agendeai.dto.SchedulingCreateDTO;
import com.agendeai.dto.SchedulingResponseDTO;
import com.agendeai.exception.SchedulingConflictException;
import com.agendeai.model.Scheduling;
import com.agendeai.repository.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final SchedulingRepository schedulingRepository;

    private final ClientService clientService;
    private final BarberService barberService;
    private final TypeServicesService typeServicesService;


    public SchedulingResponseDTO create(SchedulingCreateDTO dto){

        if (hasConflict(dto)){
            throw new RuntimeException("Horário indisponível para esse barbeiro.");
        }
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

    private boolean hasConflict(SchedulingCreateDTO dto){
        var service = typeServicesService.findById(dto.getTypeServicesId());

        List<Scheduling> existingSchedules = schedulingRepository.findByBarberId(dto.getBarberId());

        return existingSchedules.stream().anyMatch(s -> {
            LocalDateTime start = s.getDateTime();
            LocalDateTime end = start.plusMinutes(s.getTypeServices().getDurationMinutes());

            LocalDateTime newStart = dto.getDateTime();
            LocalDateTime newEnd = newStart.plusMinutes(typeServicesService.findById(dto.getTypeServicesId()).getDurationMinutes());

            return newStart.isBefore(end) && newEnd.isAfter(start);
        });
    }

    public List<LocalDateTime> getAvailableSlots(Long barberId, LocalDate date) {
        var existingSchedules = schedulingRepository.findByBarberId(barberId);

        //Pega todos os horários já agendados para esse barbeiro nessa data
        List<LocalDateTime> occupied = existingSchedules.stream()
                .filter(s -> s.getDateTime().toLocalDate().equals(date))
                .flatMap(s -> {
                    int duration = s.getTypeServices().getDurationMinutes();
                    LocalDateTime start = s.getDateTime();
                    LocalDateTime end = start.plusMinutes(duration);
                    return start.until(end, ChronoUnit.MINUTES) > 0
                            ? Stream.of(start)
                            : Stream.empty();
                })
                .toList();
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime opening = LocalDateTime.from(LocalTime.of(8,0));
        LocalDateTime closing = LocalDateTime.from(LocalTime.of(18,0));

        //Considera um intervalo de 30 minutos
        for (LocalTime time = LocalTime.from(opening); time.isBefore(LocalTime.from(closing)); time = time.plusMinutes(30)){
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            boolean isAvalaible = occupied.stream()
                    .noneMatch(occ -> occ.equals(dateTime));
            if (isAvalaible && dateTime.isAfter(LocalDateTime.now())){
                availableSlots.add(dateTime);
            }
        }
        return availableSlots;
    }
}
