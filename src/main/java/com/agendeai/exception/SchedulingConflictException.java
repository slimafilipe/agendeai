package com.agendeai.exception;

public class SchedulingConflictException extends RuntimeException {
    public SchedulingConflictException() {
        super("O barbeiro já tem um agendamento para esse horário");
    }
}
