package com.project.ecopraia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.lixeira.AtualizarLixeiraDTO;
import com.project.ecopraia.entity.dtos.lixeira.CriarLixeiraDTO;
import com.project.ecopraia.entity.enums.TipoEvento;
import com.project.ecopraia.repository.InformativoTipoRepository;
import com.project.ecopraia.repository.LixeiraRepository;

@ExtendWith(MockitoExtension.class)
public class LixeiraServiceTest {

    @Mock
    private LixeiraRepository lixeiraRepository;

    @Mock
    private InformativoTipoRepository informativoTipoRepository;

    @Mock
    private HistoricoService historicoService;

    @InjectMocks
    private LixeiraService lixeiraService;

    private Usuario usuarioLogado;
    private InformativoTipo tipoPlastico;
    private InformativoTipo tipoVidro;
    private List<InformativoTipo> listaInformativos;
    private SecurityContext securityContext;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Inicializa dados fakes comuns para evitar repetição nos testes
        usuarioLogado = new Usuario();
        usuarioLogado.setId(77L);
        usuarioLogado.setNome("Luiz Felipe");

        tipoPlastico = new InformativoTipo();
        tipoPlastico.setId(1L);
        tipoPlastico.setNomeTipo("Plástico");
        tipoPlastico.setInformativo("Garrafas PET e sacolas limpas.");
        tipoPlastico.setCor("Vermelho");

        tipoVidro = new InformativoTipo();
        tipoVidro.setId(2L);
        tipoVidro.setNomeTipo("Vidro");
        tipoVidro.setInformativo("Garrafas e potes de vidro vazios.");
        tipoVidro.setCor("Verde");

        listaInformativos = List.of(tipoPlastico, tipoVidro);
    }

    @Test
    void deveBuscarLixeiraPorIdComSucesso() {
        Long idExistente = 100L;
        Lixeira lixeira = new Lixeira();
        lixeira.setId(idExistente);

        when(lixeiraRepository.findById(idExistente)).thenReturn(Optional.of(lixeira));

        Lixeira resultado = lixeiraService.buscarPorId(idExistente);

        assertNotNull(resultado);
        assertEquals(idExistente, resultado.getId());
        verify(lixeiraRepository, times(1)).findById(idExistente);
    }

    @Test
    void deveLancarExcecaoAoBuscarLixeiraInexistente() {
        Long idInexistente = 999L;
        when(lixeiraRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            lixeiraService.buscarPorId(idInexistente);
        });

        assertEquals("Lixeira não encontrada", excecao.getMessage());
        verify(lixeiraRepository, times(1)).findById(idInexistente);
    }

    @Test
    void deveListarTodasAsLixeiras() {
        List<Lixeira> lixeirasFakes = List.of(new Lixeira(), new Lixeira());
        when(lixeiraRepository.findAll()).thenReturn(lixeirasFakes);

        List<Lixeira> resultado = lixeiraService.listarTodos();

        assertEquals(2, resultado.size());
        verify(lixeiraRepository, times(1)).findAll();
    }

    @Test
    void deveCriarLixeiraComSucesso() {
        CriarLixeiraDTO dto = new CriarLixeiraDTO(-27.59, -48.54, List.of(1L, 2L));

        securityContext = Mockito.mock(SecurityContext.class);
        authentication = Mockito.mock(Authentication.class);

        Lixeira lixeiraSalva = new Lixeira();
        lixeiraSalva.setId(500L);
        lixeiraSalva.setLatitude(dto.getLatitude());
        lixeiraSalva.setLongitude(dto.getLongitude());
        lixeiraSalva.setInformativosTipos(listaInformativos);

        when(informativoTipoRepository.findAllById(dto.getInformativosTiposIds())).thenReturn(listaInformativos);
        when(lixeiraRepository.save(any(Lixeira.class))).thenReturn(lixeiraSalva);

        try (MockedStatic<SecurityContextHolder> mockedSecurity = Mockito.mockStatic(SecurityContextHolder.class)) {
            when(authentication.getPrincipal()).thenReturn(usuarioLogado);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            Lixeira resultado = lixeiraService.criar(dto);

            assertNotNull(resultado);
            assertEquals(500L, resultado.getId());
            assertEquals(-27.59, resultado.getLatitude());
            assertEquals("Vermelho", resultado.getInformativosTipos().get(0).getCor());

            verify(informativoTipoRepository, times(1)).findAllById(dto.getInformativosTiposIds());
            verify(lixeiraRepository, times(1)).save(any(Lixeira.class));
            verify(historicoService, times(1)).registrarEvento(77L, 500L, TipoEvento.CRIACAO);
        }
    }

    @Test
    void deveAtualizarLixeiraComSucesso() {
        Long idExistente = 500L;

        AtualizarLixeiraDTO dto = new AtualizarLixeiraDTO();
        dto.setLatitude(-27.60);
        dto.setLongitude(-48.55);
        dto.setInformativosTiposIds(List.of(1L, 2L));

        Lixeira lixeiraAntiga = new Lixeira();
        lixeiraAntiga.setId(idExistente);
        lixeiraAntiga.setLatitude(-27.59);
        lixeiraAntiga.setLongitude(-48.54);

        securityContext = Mockito.mock(SecurityContext.class);
        authentication = Mockito.mock(Authentication.class);

        when(lixeiraRepository.findById(idExistente)).thenReturn(Optional.of(lixeiraAntiga));
        when(informativoTipoRepository.findAllById(dto.getInformativosTiposIds())).thenReturn(listaInformativos);
        when(lixeiraRepository.save(any(Lixeira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<SecurityContextHolder> mockedSecurity = Mockito.mockStatic(SecurityContextHolder.class)) {
            when(authentication.getPrincipal()).thenReturn(usuarioLogado);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            Lixeira resultado = lixeiraService.atualizar(idExistente, dto);

            assertNotNull(resultado);
            assertEquals(idExistente, resultado.getId());
            assertEquals(-27.60, resultado.getLatitude()); // Novo valor
            assertEquals(-48.55, resultado.getLongitude()); // Novo valor

            verify(lixeiraRepository, times(1)).findById(idExistente);
            verify(lixeiraRepository, times(1)).save(any(Lixeira.class));
            verify(historicoService, times(1)).registrarEvento(77L, idExistente, TipoEvento.EDICAO);
        }
    }

    @Test
    void deveDeletarLixeiraComSucesso() {
        Long idExistente = 500L;

        Lixeira lixeiraNoBanco = new Lixeira();
        lixeiraNoBanco.setId(idExistente);
        lixeiraNoBanco.setInformativosTipos(new ArrayList<>(listaInformativos));

        securityContext = Mockito.mock(SecurityContext.class);
        authentication = Mockito.mock(Authentication.class);

        when(lixeiraRepository.findById(idExistente)).thenReturn(Optional.of(lixeiraNoBanco));

        try (MockedStatic<SecurityContextHolder> mockedSecurity = Mockito.mockStatic(SecurityContextHolder.class)) {
            when(authentication.getPrincipal()).thenReturn(usuarioLogado);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            lixeiraService.deletar(idExistente);

            assertTrue(lixeiraNoBanco.getInformativosTipos().isEmpty());
            verify(lixeiraRepository, times(1)).findById(idExistente);
            verify(lixeiraRepository, times(1)).save(lixeiraNoBanco);
            verify(lixeiraRepository, times(1)).delete(lixeiraNoBanco);
            verify(historicoService, times(1)).registrarEvento(77L, idExistente, TipoEvento.EXCLUSAO);
        }
    }

    @Test
    void deveLancarExcecaoAoTentarDeletarLixeiraInexistente() {
        Long idInexistente = 999L;

        securityContext = Mockito.mock(SecurityContext.class);
        authentication = Mockito.mock(Authentication.class);

        when(lixeiraRepository.findById(idInexistente)).thenReturn(Optional.empty());

        try (MockedStatic<SecurityContextHolder> mockedSecurity = Mockito.mockStatic(SecurityContextHolder.class)) {
            when(authentication.getPrincipal()).thenReturn(usuarioLogado);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
                lixeiraService.deletar(idInexistente);
            });

            assertEquals("Lixeira não encontrada", excecao.getMessage());
            verify(lixeiraRepository, times(1)).findById(idInexistente);
            verify(lixeiraRepository, never()).save(any(Lixeira.class));
            verify(lixeiraRepository, never()).delete(any(Lixeira.class));
            verify(historicoService, never()).registrarEvento(any(), any(), any());
        }
    }
}