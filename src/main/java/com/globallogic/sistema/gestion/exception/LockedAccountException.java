package com.globallogic.sistema.gestion.exception;

import lombok.Getter;

import java.time.Duration;


public class LockedAccountException extends RuntimeException {
    @Getter
    private final Duration lockDuration;
    public LockedAccountException(String message, Duration lockDuration) {
        super(message);
        this.lockDuration = lockDuration;
    }
}
