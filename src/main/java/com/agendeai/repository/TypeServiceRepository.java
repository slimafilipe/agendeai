package com.agendeai.repository;

import com.agendeai.model.TypeServices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeServiceRepository extends JpaRepository<TypeServices, Long> {
    Optional<TypeServices> findByBarberId(Long barberId);
}
