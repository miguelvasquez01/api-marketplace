package com.marketplace.api.services;

import lombok.Data;

import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public class LoginRequest {

    private String nombre;
    private String cedula;

    private static final Logger LOGGER = Logger.getLogger(LoginRequest.class.getName());

    static {
        LoggerConfigService.getInstance().configureLogger(LOGGER);
    }

    // Constructor
    public LoginRequest(String username, String password) {
        this.nombre = username;
        this.cedula = password;
        LOGGER.log(Level.INFO, "LoginRequest creado con nombre: " + nombre + " y cédula: " + cedula);
    }

    public LoginRequest() {
        LOGGER.log(Level.INFO, "LoginRequest creado sin parámetros");
    }
}
