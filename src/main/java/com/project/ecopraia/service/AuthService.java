package com.project.ecopraia.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.TokenRedefinicaoSenha;
import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.auth.EsqueciSenhaDTO;
import com.project.ecopraia.entity.dtos.auth.EsqueciSenhaResponseDTO;
import com.project.ecopraia.entity.dtos.auth.LoginDTO;
import com.project.ecopraia.entity.dtos.auth.LoginResponseDTO;
import com.project.ecopraia.entity.dtos.auth.RedefinirSenhaDTO;
import com.project.ecopraia.exception.CredenciaisInvalidasException;
import com.project.ecopraia.exception.RecursoNaoEncontradoException;
import com.project.ecopraia.exception.RegraDeNegocioException;
import com.project.ecopraia.repository.TokenRedefinicaoSenhaRepository;
import com.project.ecopraia.repository.UsuarioRepository;

@Service
public class AuthService {

    private static final long EXPIRACAO_TOKEN_HORAS = 1;

    private final UsuarioRepository repository;
    private final TokenRedefinicaoSenhaRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository repository,
            TokenRedefinicaoSenhaRepository tokenRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {

        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginDTO dto) {

        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new CredenciaisInvalidasException("E-mail ou senha inválidos")
                );

        boolean senhaCorreta = passwordEncoder.matches(
                dto.getSenha(),
                usuario.getSenha()
        );

        if (!senhaCorreta) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(token);
    }

    public EsqueciSenhaResponseDTO esqueciSenha(EsqueciSenhaDTO dto) {

        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Não encontramos uma conta com este e-mail")
                );

        String token = UUID.randomUUID().toString();

        TokenRedefinicaoSenha tokenRedefinicao = new TokenRedefinicaoSenha(
                token,
                usuario,
                LocalDateTime.now().plusHours(EXPIRACAO_TOKEN_HORAS)
        );

        tokenRepository.save(tokenRedefinicao);

        return new EsqueciSenhaResponseDTO(token);
    }

    public void redefinirSenha(RedefinirSenhaDTO dto) {

        TokenRedefinicaoSenha tokenRedefinicao = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() ->
                        new RegraDeNegocioException("Token inválido")
                );

        if (tokenRedefinicao.isUsado()) {
            throw new RegraDeNegocioException("Este token já foi utilizado");
        }

        if (tokenRedefinicao.isExpirado()) {
            throw new RegraDeNegocioException("Este token expirou. Solicite a redefinição novamente");
        }

        Usuario usuario = tokenRedefinicao.getUsuario();
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        repository.save(usuario);

        tokenRedefinicao.setUsado(true);
        tokenRepository.save(tokenRedefinicao);
    }
}
