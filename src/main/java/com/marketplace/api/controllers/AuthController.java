package com.marketplace.api.controllers;

import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.services.AuthService;
import com.marketplace.api.services.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/Vendedor")
    public Vendedor autenticarVendedor(@RequestBody LoginRequest loginRequest) {
        return authService.autenticarVendedor(loginRequest);
    }
}
