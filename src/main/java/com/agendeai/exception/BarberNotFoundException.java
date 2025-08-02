package com.agendeai.exception;

public class BarberNotFoundException extends RuntimeException {
    public BarberNotFoundException(Long id) {
        super("Barbeiro com o ID " + id + " não encontrado");
    }
}
