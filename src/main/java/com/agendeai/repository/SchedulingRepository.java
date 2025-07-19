package com.agendeai.repository;

import com.agendeai.model.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
    boolean existsByBarberIdAndDateTime(Long barberId, LocalDateTime dateTime);

    List<Scheduling> findByBarberId(Long barberId);
    List<Scheduling> findByClientId(Long clientId);
}
