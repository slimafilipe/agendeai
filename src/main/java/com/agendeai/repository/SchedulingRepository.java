package com.agendeai.repository;

import com.agendeai.model.Barber;
import com.agendeai.model.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
    List<Scheduling> findByBarberIdAndDateTime(Long barberId, LocalDate dateTime);

    List<Scheduling> findByBarberId(Long barberId);
    List<Scheduling> findByClientId(Long clientId);

    List<Scheduling> findByBarberAndStartBetween(Barber barber, LocalDateTime start, LocalDateTime end);
}
