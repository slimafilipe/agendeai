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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.test.util.ReflectionTestUtils;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SchedulingServiceTest {
    private TypeServiceRepository typeServiceRepository;
    private  SchedulingRepository schedulingRepository;

    private  ClientService clientService;
    private  BarberService barberService;
    private TypeServicesService typeServicesService;
    private SchedulingService schedulingService;

    @BeforeEach
    void setUp() {
        schedulingRepository = mock(SchedulingRepository.class);
        typeServiceRepository = mock(TypeServiceRepository.class);
        clientService = mock(ClientService.class);
        barberService = mock(BarberService.class);
        typeServicesService = mock(TypeServicesService.class);
        schedulingService = new SchedulingService(
                typeServiceRepository,
                schedulingRepository,
                clientService,
                barberService,
                typeServicesService
                );
    }
    @Test
    public void testCreateScheduling_Success() {
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
        when(schedulingRepository.save(any(Scheduling.class))).thenAnswer(invocation -> invocation.getArguments(0));


        SchedulingResponseDTO responseDTO = schedulingService.create(dto);

        assertNotNull(responseDTO);
        assertEquals(2L, responseDTO.getBarberId());
        assertEquals(1L, responseDTO.getClientId());
    }

    @Test
    public void testCreateScheduling_Conflict() {
        SchedulingCreateDTO dto = new SchedulingCreateDTO();
        dto.setClientId(1L);
        dto.setBarberId(2L);
        dto.setTypeServicesId(3L);
        dto.setDateTime(LocalDateTime.of(2025, 8, 7, 10,  0));

        Client client = new Client(); client.setId(1L);
        Barber barber = new Barber();barber.setId(2L);
        TypeServices service = new TypeServices(); service.setId(3L); service.setDurationMinutes(30);

        Scheduling existing = new Scheduling();
        existing.setDateTime(LocalDateTime.of(2025, 8, 7, 10, 15)); // Overlapping

        when(clientService.findById(1L)).thenReturn(client);
        when(barberService.findById(2L)).thenReturn(barber);
        when(typeServicesService.findById(3L)).thenReturn(service);
        when(schedulingRepository.findByBarberAndStartBetween(any(),any(),any())).thenReturn(Collections.singletonList(existing));

        assertThrows(IllegalArgumentException.class, () -> schedulingService.create(dto));
    }

    @Test
    void shouldReturnAvailableSlotsForBarberOnDate() {
        Long barberId = 1L;
        LocalDate date = LocalDate.of(2025, 8, 7);

        Barber barber = new Barber();
        barber.setId(barberId);

        when(barberService.findById(barberId)).thenReturn(barber);

        // Simulando agendamento das 10h às 10h30
        Scheduling existing = new Scheduling();
       // ReflectionTestUtils.setField(existing, "datetime", LocalDateTime.of(date, LocalTime.of(10, 0)));
        //ReflectionTestUtils.setField(existing, "datetime", LocalDateTime.of(date, LocalTime.of(10, 30)));

        when(schedulingRepository.findByBarberAndStartBetween(
                eq(barber),
                eq(date.atTime(8, 0)),
                eq(date.atTime(18, 0))
        )).thenReturn(List.of(existing));

        // Simulando serviço de 30 minutos
        TypeServices service = new TypeServices();
        service.setDurationMinutes(30);

        when(typeServiceRepository.findByBarberId(barberId)).thenReturn(List.of(service));

        List<LocalDateTime> availableSlots = schedulingService.getAvailableSlots(barberId, date, 30L);

        // Esperamos que o horário das 10:00 não esteja disponível, mas os outros sim
        assertFalse(availableSlots.contains(LocalDateTime.of(date, LocalTime.of(10, 0))));
        assertTrue(availableSlots.contains(LocalDateTime.of(date, LocalTime.of(10, 30))));
        assertTrue(availableSlots.contains(LocalDateTime.of(date, LocalTime.of(11, 0))));
    }

}
