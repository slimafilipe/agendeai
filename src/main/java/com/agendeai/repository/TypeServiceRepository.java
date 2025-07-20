package com.agendeai.repository;

import com.agendeai.model.TypeServices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TypeServiceRepository extends JpaRepository<TypeServices, Long> {
    List<TypeServices> findByBarberId(Long barberId);
    boolean existsByNameServiceAndBarberId(String nameService, Long barberId);
}
