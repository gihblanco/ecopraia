package com.project.ecopraia.controller;

import org.springframework.web.bind.annotation.*;

import com.project.ecopraia.entity.dtos.auth.LoginDTO;
import com.project.ecopraia.entity.dtos.auth.LoginResponseDTO;
import com.project.ecopraia.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginDTO dto
    ) {

        return service.login(dto);
    }
}