package com.agendeai;

import com.agendeai.dto.SchedulingCreateDTO;
import com.agendeai.dto.SchedulingResponseDTO;
import com.agendeai.model.Barber;
import com.agendeai.model.Client;
import com.agendeai.model.Scheduling;
import com.agendeai.model.TypeServices;
import com.agendeai.repository.SchedulingRepository;
import com.agendeai.repository.TypeServiceRepository;
import com.agendeai.service.BarberService;
import com.agendeai.service.ClientService;
import com.agendeai.service.SchedulingService;
import com.agendeai.service.TypeServicesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SchedulingServiceTest {

    @Mock
    private TypeServiceRepository typeServiceRepository;
    @Mock
    private  SchedulingRepository schedulingRepository;
    @Mock
    private  ClientService clientService;
    @Mock
    private  BarberService barberService;
    @Mock
    private TypeServicesService typeServicesService;

    @InjectMocks
    private SchedulingService schedulingService;


    @Test
    @DisplayName("Should create a scheduling successfully when the slot is available")
    public void testCreateScheduling_Success() {
        // Given (Arrange)
        LocalDateTime scheduleTime = LocalDateTime.of(2025, 8, 7, 10, 0);
        SchedulingCreateDTO dto = new SchedulingCreateDTO();
        dto.setClientId(1L);
        dto.setBarberId(2L);
        dto.setTypeServicesId(3L);
        dto.setDateTime(LocalDateTime.of(2025, 8, 7, 10,  0));

        Client client = new Client(); client.setId(1L);
        Barber barber = new Barber();barber.setId(2L);
        TypeServices service = new TypeServices(); service.setId(1L); service.setDurationMinutes(30);

        when(clientService.findById(1L)).thenReturn(client);
        when(barberService.findById(2L)).thenReturn(barber);
        when(typeServicesService.findById(3L)).thenReturn(service);
        when(schedulingRepository.findByBarberAndStartBetween(any(), any(), any())).thenReturn(Collections.emptyList());
        when(schedulingRepository.save(any(Scheduling.class))).thenAnswer(invocation -> {
            Scheduling savedScheduling = invocation.getArgument(0);
            savedScheduling.setId(100L); // Simulate saving and getting an ID
            return savedScheduling;
        });


        // When(Act)
        SchedulingResponseDTO responseDTO = schedulingService.create(dto);

        assertNotNull(responseDTO);
        assertEquals(2L, responseDTO.getBarberId());
        assertEquals(1L, responseDTO.getClientId());
        assertEquals(scheduleTime, responseDTO.getDateTime());
    }

    @Test
    @DisplayName("Should throw an exception when creating a scheduling for a busy slot")
    public void testCreateScheduling_Conflict() {
        // Given (Arrange)
        LocalDateTime scheduleTime = LocalDateTime.of(2025, 8, 7, 10, 0);
        SchedulingCreateDTO dto = new SchedulingCreateDTO();
        dto.setClientId(1L);
        dto.setBarberId(2L);
        dto.setTypeServicesId(3L);
        dto.setDateTime(LocalDateTime.of(2025, 8, 7, 10,  0));

        Client client = new Client(); client.setId(1L);
        Barber barber = new Barber();barber.setId(2L);
        TypeServices service = new TypeServices(); service.setId(3L); service.setDurationMinutes(30);

        Scheduling existingScheduling = new Scheduling();
        existingScheduling.setDateTime(LocalDateTime.of(2025, 8, 7, 9, 45));
        existingScheduling.setTypeServices(service);

        when(clientService.findById(1L)).thenReturn(client);
        when(barberService.findById(2L)).thenReturn(barber);
        when(typeServicesService.findById(3L)).thenReturn(service);
        when(schedulingRepository.findByBarberAndStartBetween(any(Barber.class),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(Collections.singletonList(existingScheduling));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            schedulingService.create(dto);
        });
        assertEquals("Horário indisponível para esse barbeiro", exception.getMessage());
    }

    @Test
    @DisplayName("Should return available slots for a barber on a given date")
    void shouldReturnAvailableSlotsForBarberOnDate() {
        Long barberId = 1L;
        LocalDate date = LocalDate.of(2025, 8, 7);
        long serviceId = 3L;
        long serviceDuration = 30;

        Barber barber = new Barber();
        barber.setId(barberId);

        TypeServices mockService = new TypeServices();
        mockService.setId(serviceId);
        mockService.setDurationMinutes(30);

        when(typeServiceRepository.findById(serviceId)).thenReturn(Optional.of(mockService));

        TypeServices serviceToFind = new TypeServices();
        serviceToFind.setId(serviceId);
        serviceToFind.setDurationMinutes((int) serviceDuration);

       // when(typeServicesService.findById(serviceId)).thenReturn(serviceToFind);

        TypeServices existingService = new TypeServices();
        existingService.setDurationMinutes(30);
//        when(barberService.findById(barberId)).thenReturn(barber);

        // Simulando agendamento das 10h às 10h30
        Scheduling existingAppointment = new Scheduling();
        existingAppointment.setBarber(barber);
        existingAppointment.setDateTime(LocalDateTime.of(date, LocalTime.of(10,0)));
        existingAppointment.setTypeServices(existingService);

       // when(barberService.findById(barberId)).thenReturn(barber);

        LocalDateTime dayStart = date.atTime(8,0);
        LocalDateTime dayEnd = date.atTime(18,0);
        /*when(schedulingRepository.findByBarberAndStartBetween(
                barber,
                dayStart,
                dayEnd
        )).thenReturn(List.of(existingAppointment));

         */
        List<LocalDateTime> availableSlots = schedulingService.getAvailableSlots(barberId, date, serviceId);

        assertNotNull(availableSlots);

        //assertFalse(availableSlots.contains(LocalDateTime.of(date, LocalTime.of(10, 0))));
        assertTrue(availableSlots.contains(LocalDateTime.of(date, LocalTime.of(10, 30))));
        assertTrue(availableSlots.contains(LocalDateTime.of(date, LocalTime.of(11, 0))));
    }

}
