package com.project.ecopraia.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
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

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.dtos.informativo.AtualizarInformativoTipoDTO;
import com.project.ecopraia.entity.dtos.informativo.CriarInformativoTipoDTO;
import com.project.ecopraia.repository.InformativoTipoRepository;

@ExtendWith(MockitoExtension.class)
public class InformativoTipoServiceTest {

    @Mock
    private InformativoTipoRepository informativoTipoRepository;

    @InjectMocks
    private InformativoTipoService informativoTipoService;

    private InformativoTipo informativoExistente;
    private InformativoTipo informativoTipo2;
    private CriarInformativoTipoDTO criarDto;
    private AtualizarInformativoTipoDTO atualizarDto;

    @BeforeEach
    void setUp() {
        informativoExistente = new InformativoTipo(1L, "Plástico", "Descarte garrafas PET", "Vermelho");
        informativoTipo2 = new  InformativoTipo(2L, "papel", "Caixas de papelão, jornais, revistas, etc. Lixeira não recomendada para guardanapos ou papel higiênico", "azul");

        criarDto = new CriarInformativoTipoDTO();
        criarDto.setNomeTipo("Vidro");
        criarDto.setInformativo("Descarte garrafas de vidro quebradas ou inteiras");
        criarDto.setCor("Verde");

        atualizarDto = new AtualizarInformativoTipoDTO();
        atualizarDto.setNomeTipo("Plástico Modificado");
        atualizarDto.setInformativo("Apenas plásticos recicláveis");
        atualizarDto.setCor("Vermelho Escuro");
    }

    @Test
    void deveBuscarInformativoPorIdComSucesso() {
        Long idInformativo = 1L;
        when(informativoTipoRepository.findById(idInformativo)).thenReturn(Optional.of(informativoExistente));

        InformativoTipo resultado = informativoTipoService.buscarPorId(idInformativo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Plástico", resultado.getNomeTipo());
        verify(informativoTipoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarexcecaoQuandoInformativoNaoForEncontrado() {
        Long idInexistente = 99L;
        when(informativoTipoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            informativoTipoService.buscarPorId(idInexistente);
        });

        assertEquals("Informativo não encontrado", excecao.getMessage());
        verify(informativoTipoRepository, times(1)).findById(99L);
    }

    @Test
    void deveListarTodosOsInformativosComSucesso() {
        when(informativoTipoRepository.findAll()).thenReturn(List.of(informativoExistente, informativoTipo2));

        List<InformativoTipo> resultado = informativoTipoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Plástico", resultado.get(0).getNomeTipo());
        verify(informativoTipoRepository, times(1)).findAll();
    }

    @Test
    void deveCriarInformativoTipoComSucesso() {
        InformativoTipo informativoSalvo = new InformativoTipo(2L, criarDto.getNomeTipo(), criarDto.getInformativo(), criarDto.getCor());
        when(informativoTipoRepository.save(any(InformativoTipo.class))).thenReturn(informativoSalvo);

        InformativoTipo resultado = informativoTipoService.criar(criarDto);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("Vidro", resultado.getNomeTipo());
        assertEquals("Verde", resultado.getCor());
        verify(informativoTipoRepository, times(1)).save(any(InformativoTipo.class));
    }

    @Test
    void deveAtualizarInformativoTipoComSucesso() {
        Long idExistente = 1L;
        when(informativoTipoRepository.findById(idExistente)).thenReturn(Optional.of(informativoExistente));
        when(informativoTipoRepository.save(any(InformativoTipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InformativoTipo resultado = informativoTipoService.atualizar(1L, atualizarDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Plástico Modificado", resultado.getNomeTipo());
        assertEquals("Apenas plásticos recicláveis", resultado.getInformativo());
        verify(informativoTipoRepository, times(1)).findById(idExistente);
        verify(informativoTipoRepository, times(1)).save(any(InformativoTipo.class));
    }

    @Test
    void deveExcluirInformativoComSucesso() {
        long idExistente = 3L;

        when(informativoTipoRepository.existsById(idExistente)).thenReturn(true);
        
        informativoTipoService.excluir(idExistente);

        verify(informativoTipoRepository, times(1)).existsById(idExistente);
        verify(informativoTipoRepository, times(1)).deleteById(3L);
    }

    @Test
    void deveLancarExcecaoAoTentarExcluirInformativoInexistente() {
        Long idInexistente = 99L;

        when(informativoTipoRepository.existsById(idInexistente)).thenReturn(false);

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            informativoTipoService.excluir(idInexistente);
        });

        assertEquals("Informativo não encontrado", excecao.getMessage());
        verify(informativoTipoRepository, times(1)).existsById(idInexistente);
        verify(informativoTipoRepository, never()).deleteById(idInexistente);
    }
}