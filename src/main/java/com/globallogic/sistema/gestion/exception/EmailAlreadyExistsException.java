package com.globallogic.sistema.gestion.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends RuntimeException {

    private final String email;

    public EmailAlreadyExistsException(String email){
        super("Invalid email, user with email: " + email + "already exists");
        this.email = email;
    }

}
