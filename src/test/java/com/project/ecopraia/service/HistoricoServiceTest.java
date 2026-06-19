package com.project.ecopraia.service;

import java.time.LocalDateTime;
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

import com.project.ecopraia.entity.Historico;
import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.enums.TipoEvento;
import com.project.ecopraia.repository.HistoricoRepository;
import com.project.ecopraia.repository.LixeiraRepository;
import com.project.ecopraia.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class HistoricoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LixeiraRepository lixeiraRepository;

    @Mock
    private HistoricoRepository historicoRepository;

    @InjectMocks
    private HistoricoService historicoService;

    @Test
    void deveConseguirCriarUmEventoDeHistoricoComSucesso() {
        Long historicoId = 3L;
        Long lixeiraId = 4L;
        Lixeira lixeira = new Lixeira();
        lixeira.setId(lixeiraId);

        Long usuarioId = 5L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Historico historico = new Historico();

        when(lixeiraRepository.findById(lixeiraId)).thenReturn(Optional.of(lixeira));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(historicoRepository.save(any(Historico.class))).thenReturn(historico);

        historico.setId(historicoId);
        historico.setLixeira(lixeira);
        historico.setUsuario(usuario);
        historico.setTipoEvento(TipoEvento.CRIACAO);
        historico.setDataHora(LocalDateTime.parse("2026-06-16T20:56:20"));

        Historico historicoResultado = historicoService.registrarEvento(usuarioId, lixeiraId, TipoEvento.CRIACAO);

        assertEquals(3L, historicoResultado.getId());
        assertEquals(TipoEvento.CRIACAO, historicoResultado.getTipoEvento());
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(lixeiraRepository, times(1)).findById(lixeiraId);
        verify(historicoRepository, times(1)).save(any(Historico.class));
    }

    @Test
    void deveEncontrarUmEventoEspecificoComSucessoPeloSeuId() {
        Long idExistente = 5L;
        Historico novoEvento = new Historico();
        novoEvento.setId(idExistente);
        novoEvento.setTipoEvento(TipoEvento.CRIACAO);

        when(historicoRepository.findById(idExistente)).thenReturn(Optional.of(novoEvento));

        Historico historicoResultado = historicoService.buscarPorId(idExistente);

        assertEquals(5L, historicoResultado.getId());
        assertEquals(TipoEvento.CRIACAO, historicoResultado.getTipoEvento());
        verify(historicoRepository, times(1)).findById(idExistente);
    }

    @Test
    void deveLancarExcecaoQuandoOIdDeEventoNaoExistir() {
        Long idInexistente = 6L;

        Historico novoEvento = new Historico();
        novoEvento.setId(idInexistente);
        novoEvento.setTipoEvento(TipoEvento.EDICAO);

        when(historicoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            historicoService.buscarPorId(idInexistente);
        });

        assertEquals("Historico não encontrado", excecao.getMessage());
        verify(historicoRepository, times(1)).findById(idInexistente);
    }

    @Test
    void deveExcluirUmHistoricoComSucesso() {
        Long idExistente = 35L;

        when(historicoRepository.existsById(idExistente)).thenReturn(true);

        historicoService.excluir(idExistente);

        verify(historicoRepository, times(1)).existsById(idExistente);
        verify(historicoRepository, times(1)).deleteById(idExistente);
    }

    @Test
    void deveRetornarExcecaoAoTentarExcluirHistoricoInexistente() {
        Long idInexistente = 7L;

        when(historicoRepository.existsById(idInexistente)).thenReturn(false);

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            historicoService.excluir(idInexistente);
        });

        assertEquals("Historico não encontrado", excecao.getMessage());
        verify(historicoRepository, times(1)).existsById(idInexistente);
        verify(historicoRepository, never()).deleteById(idInexistente);
    }
}
