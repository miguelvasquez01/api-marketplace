package com.marketplace.api.services;

import lombok.Data;

@Data
public class LoginRequest {

    private String nombre;
    private String cedula;

    // Constructor
    public LoginRequest(String username, String password) {
        this.nombre = username;
        this.cedula = password;
    }

    public LoginRequest() {
    }
}
