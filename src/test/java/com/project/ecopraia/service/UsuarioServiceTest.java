package com.project.ecopraia.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.usuario.AtualizarSenhaUsuarioDTO;
import com.project.ecopraia.entity.dtos.usuario.AtualizarUsuarioDTO;
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

    @Test
    void deveConseguirAtualizarUsuarioComSucesso() {
        Long idExistente = 34L;
        AtualizarUsuarioDTO usuarioEditado = new AtualizarUsuarioDTO("Luiz Modificado", "luiznovo43@gmail.com");

        Usuario usuarioAntigo = new Usuario();
        usuarioAntigo.setId(idExistente);
        usuarioAntigo.setNome("Luiz Felipe");
        usuarioAntigo.setEmail("lipe_42@gmail.com");

        when(usuarioRepository.findById(idExistente)).thenReturn(Optional.of(usuarioAntigo));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioResultado = usuarioService.atualizar(idExistente, usuarioEditado);

        assertEquals(34L, usuarioResultado.getId());
        assertEquals("Luiz Modificado", usuarioResultado.getNome());
        assertEquals("luiznovo43@gmail.com", usuarioResultado.getEmail());
        verify(usuarioRepository, times(1)).findById(idExistente);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveConseguirAtualizarECriptografarASenhaDoUsuarioComSucesso() {
        Long idExistente = 35L;
        AtualizarSenhaUsuarioDTO novaSenha = new AtualizarSenhaUsuarioDTO("senha1245");

        Usuario usuarioAntigo = new Usuario();
        usuarioAntigo.setId(idExistente);
        usuarioAntigo.setNome("Luiz Felipe");
        usuarioAntigo.setEmail("lipe_42@gmail.com");
        usuarioAntigo.setSenha("senhacriptografadafalsa123");

        when(usuarioRepository.findById(idExistente)).thenReturn(Optional.of(usuarioAntigo));
        when(passwordEncoder.encode("senha1245")).thenReturn("senhacriptografada1245");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioComSenhaAtualizada = usuarioService.atualizarSenha(idExistente, novaSenha);

        assertEquals(35L, usuarioComSenhaAtualizada.getId());
        assertEquals("senhacriptografada1245", usuarioComSenhaAtualizada.getSenha());
        verify(usuarioRepository, times(1)).findById(idExistente);
        verify(passwordEncoder, times(1)).encode("senha1245");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveConseguirExcluirUsuarioComSucesso() {
        Long idExistente = 35L;

        when(usuarioRepository.existsById(idExistente)).thenReturn(true);

        usuarioService.excluir(idExistente);

        verify(usuarioRepository, times(1)).deleteById(idExistente);
    }

    @Test
    void deveLancarExcecaoAoTentarExcluirUsuarioInexistente() {
        Long idInexistente = 89L;

        when(usuarioRepository.existsById(idInexistente)).thenReturn(false);

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            usuarioService.excluir(idInexistente);
        });

        assertEquals("Usuário não encontrado para exclusão", excecao.getMessage());
        verify(usuarioRepository, times(1)).existsById(idInexistente);
        verify(usuarioRepository, never()).deleteById(idInexistente);
    }
}