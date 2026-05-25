package com.project.ecopraia.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.ecopraia.entity.Usuario;

@Service
public class JwtService {

    private final String SECRET_KEY = "ecopraia-secret-key";

    public String gerarToken(Usuario usuario) {

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(usuario.getEmail())
                .withExpiresAt(
                        Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public String validarToken(String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (Exception ex) {
            return null;
        }
    }
}