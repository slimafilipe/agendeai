package com.agendeai.exception;

public class TypeServiceNotFoundException extends RuntimeException {
    public TypeServiceNotFoundException(Long id) {
        super("Serviço com o id " + id + " não encontrado");
    }
}
