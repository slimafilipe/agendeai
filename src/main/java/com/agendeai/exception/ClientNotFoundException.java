package com.agendeai.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Cliente com o ID " + id + " não encontrado");
    }
}
