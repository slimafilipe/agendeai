package com.agendeai.service;

import com.agendeai.dto.SchedulingCreateDTO;
import com.agendeai.dto.SchedulingResponseDTO;
import com.agendeai.exception.SchedulingConflictException;
import com.agendeai.exception.TypeServiceNotFoundException;
import com.agendeai.model.Barber;
import com.agendeai.model.Scheduling;
import com.agendeai.model.TypeServices;
import com.agendeai.repository.SchedulingRepository;
import com.agendeai.repository.TypeServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
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

    private final TypeServiceRepository typeServiceRepository;
    private final SchedulingRepository schedulingRepository;

    private final ClientService clientService;
    private final BarberService barberService;
    private final TypeServicesService typeServicesService;


    public SchedulingResponseDTO create(SchedulingCreateDTO dto){

        var client = clientService.findById(dto.getClientId());
        var barber = barberService.findById(dto.getBarberId());
        var service = typeServicesService.findById(dto.getTypeServicesId());

        LocalDateTime newStart = dto.getDateTime();
        LocalDateTime newEnd = newStart.plusMinutes(service.getDurationMinutes());

        List<Scheduling> conflitScheduling = schedulingRepository.findByBarberAndStartBetween(barber, newStart, newEnd);

        if (!conflitScheduling.isEmpty()){
            throw new IllegalArgumentException("Horário indisponível para esse barbeiro");
        }
        var scheduling = new Scheduling();
        scheduling.setDateTime(newStart);
        scheduling.setClient(client);
        scheduling.setBarber(barber);
        scheduling.setTypeServices(service);
        scheduling.setStatusScheduling(Scheduling.StatusScheduling.AGENDADO);

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
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return schedulingRepository.findByDateTimeBetween(startOfDay,endOfDay)
                .stream()
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
        return schedulingRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }


    public List<LocalDateTime> getAvailableSlots(Long barberId, LocalDate date, Long serviceId) {

        TypeServices service = typeServiceRepository.findById(serviceId)
                .orElseThrow(() -> new TypeServiceNotFoundException(serviceId));
        int durationMinutes = service.getDurationMinutes();

        LocalDateTime start = date.atTime(9,0);
        LocalDateTime end = date.atTime(18,0);

        List<Scheduling> schedules = schedulingRepository.findByBarberIdAndDateTime(barberId, date);

        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime currentSlotStart = date.atTime(9,0);
        LocalDateTime dayEnd = date.atTime(18,0);
        while (!currentSlotStart.plusMinutes(durationMinutes).isAfter(dayEnd)) {
            LocalDateTime currentslotEnd = currentSlotStart.plusMinutes(durationMinutes);

            LocalDateTime finalCurrentSlotStart = currentSlotStart;
            boolean hasConflit = schedules.stream()
                    .anyMatch(existingSchedule -> {
                        LocalDateTime existingStart = existingSchedule.getDateTime();
                        LocalDateTime existingEnd = existingStart.plusMinutes(existingSchedule.getTypeServices().getDurationMinutes());

                        return finalCurrentSlotStart.isBefore(existingEnd) && currentslotEnd.isAfter(existingStart);

                    });
            if (!hasConflit) {
                availableSlots.add(currentSlotStart);
            }
            currentSlotStart = currentSlotStart.plusMinutes(30);
        }
    return availableSlots;
    }
}
