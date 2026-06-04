package com.project.ecopraia.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.usuario.CriarUsuarioDTO;
import com.project.ecopraia.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioComSucesso() {
        CriarUsuarioDTO usuarioDto = new CriarUsuarioDTO("Luiz Felipe", "lipe_42@gmail.com", "senha123");
        Usuario novoUsuario = new Usuario();

        when(passwordEncoder.encode("senha123")).thenReturn("senhacriptografadafalsa123");
        when(usuarioRepository.existsByEmail(usuarioDto.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuario);

        novoUsuario.setId(34L);
        novoUsuario.setNome((usuarioDto.getNome()));
        novoUsuario.setEmail(usuarioDto.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));

        Usuario usuarioResultado = usuarioService.criar(usuarioDto);

        assertEquals(34L, usuarioResultado.getId());
        assertEquals("Luiz Felipe", usuarioResultado.getNome());
        assertEquals("lipe_42@gmail.com", usuarioResultado.getEmail());
        assertEquals("senhacriptografadafalsa123", usuarioResultado.getSenha());
    }

    @Test
    void deveLancarExcecaoSeOIdDeUsuarioNaoExistir() {
        Long idInvalido = 59L;

        when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorId(idInvalido);
        });

        assertEquals("Usuário não encontrado", excecao.getMessage());
        verify(usuarioRepository, times(1)).findById(idInvalido);
    }

    @Test
    void deveRetornarErroSeOEmailDeUsuarioJaExistir() {
        CriarUsuarioDTO dtoEmailDuplicado = new CriarUsuarioDTO("Luiz Felipe", "lipe_42@gmail.com", "senha123");
        when(usuarioRepository.existsByEmail(dtoEmailDuplicado.getEmail())).thenReturn(true);

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            usuarioService.criar(dtoEmailDuplicado);
        });

        assertEquals("Email já cadastrado", excecao.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail("lipe_42@gmail.com");
    }
}