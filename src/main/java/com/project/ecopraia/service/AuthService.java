package com.project.ecopraia.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.auth.LoginDTO;
import com.project.ecopraia.entity.dtos.auth.LoginResponseDTO;
import com.project.ecopraia.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository repository,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginDTO dto) {

        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        boolean senhaCorreta = passwordEncoder.matches(
                dto.getSenha(),
                usuario.getSenha()
        );

        if (!senhaCorreta) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(token);
    }
}