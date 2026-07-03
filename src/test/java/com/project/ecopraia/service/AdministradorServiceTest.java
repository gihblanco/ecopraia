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

import com.project.ecopraia.entity.Administrador;
import com.project.ecopraia.entity.dtos.admin.AtualizarAdministradorDTO;
import com.project.ecopraia.entity.dtos.admin.AtualizarSenhaAdministradorDTO;
import com.project.ecopraia.entity.dtos.admin.CriarAdministradorDTO;
import com.project.ecopraia.repository.AdministradorRepository;

@ExtendWith(MockitoExtension.class)
public class AdministradorServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AdministradorService administradorService;

    @Test
    void deveEncontrarComSucessoUmAdministradorPeloId() {
        Long idExistente = 40L;

        Administrador administradorEncontrado = new Administrador();
        administradorEncontrado.setId(idExistente);
        administradorEncontrado.setNome("Carlos");

        when(administradorRepository.findById(idExistente)).thenReturn(Optional.of(administradorEncontrado));

        Administrador adminResultado = administradorService.buscarPorId(idExistente);

        assertEquals(40L, adminResultado.getId());
        assertEquals("Carlos", adminResultado.getNome());
        verify(administradorRepository, times(1)).findById(idExistente);
    }

    @Test
    void deveLancarErroAoBuscarAdministradorInexistente() {
        Long idInexistente = 500L;

        when(administradorRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            administradorService.buscarPorId(idInexistente);
        });

        assertEquals("Administrador não encontrado", excecao.getMessage());
        verify(administradorRepository, times(1)).findById(idInexistente);
    }

    @Test
    void deveLancarExcecaoSeOEmailJaExistir() {
        CriarAdministradorDTO dto = new CriarAdministradorDTO();
        dto.setNome("Carlos");
        dto.setEmail("carlos42@gmail.com");

        when(administradorRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            administradorService.criar(dto);
        });

        assertEquals("Este e-mail já está cadastrado", excecao.getMessage());
        verify(administradorRepository, never()).save(any(Administrador.class));
    }

    @Test
    void deveCriarAdminComSucesso() {
        CriarAdministradorDTO adminDto = new CriarAdministradorDTO(
            "João Miguel",
            "jm@concap.com.br",
            "senha123",
            "037264736-50",
            "concap",
            "diretor"
        );

        Long id = 50L;

        Administrador novoAdmin = new Administrador();

        when(passwordEncoder.encode("senha123")).thenReturn("senhacriptografadafalsa123123");
        when(administradorRepository.existsByEmail(adminDto.getEmail())).thenReturn(false);
        when(administradorRepository.save(any(Administrador.class))).thenReturn(novoAdmin);

        novoAdmin.setId(id);
        novoAdmin.setNome(adminDto.getNome());
        novoAdmin.setEmail(adminDto.getEmail());
        novoAdmin.setSenha("senhacriptografadafalsa123123");
        novoAdmin.setCpf(adminDto.getCpf());
        novoAdmin.setInstituicao((adminDto.getInstituicao()));
        novoAdmin.setCargo(adminDto.getCargo());

        Administrador adminResultado = administradorService.criar(adminDto);

        assertEquals(50L, adminResultado.getId());
        assertEquals("João Miguel", adminResultado.getNome());
        assertEquals("senhacriptografadafalsa123123", adminResultado.getSenha());
        assertEquals("concap", adminResultado.getInstituicao());
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(administradorRepository, times(1)).save(any(Administrador.class));
    }

    @Test
    void deveConseguirAtualizarAdministradorComSucesso() {
        Long idExistente = 30L;

        AtualizarAdministradorDTO admModificado = new AtualizarAdministradorDTO(
            "João Carlos",
            "jc43@concap.com.br",
            "concap",
            "assistente administrativo"
        );

        Administrador admAntigo = new  Administrador();

        when(administradorRepository.findById(idExistente)).thenReturn(Optional.of(admAntigo));
        when(administradorRepository.save(any(Administrador.class))).thenAnswer(invocation -> invocation.getArgument(0));

        admAntigo.setId(idExistente);
        admAntigo.setNome(admModificado.getNome());
        admAntigo.setEmail(admModificado.getEmail());
        admAntigo.setInstituicao(admModificado.getInstituicao());
        admAntigo.setCargo(admModificado.getCargo());

        Administrador admAtualizado = administradorService.atualizar(idExistente, admModificado);

        assertEquals(30L, admAtualizado.getId());
        assertEquals("João Carlos", admAtualizado.getNome());
        assertEquals("concap", admAtualizado.getInstituicao());
        assertEquals("assistente administrativo", admAtualizado.getCargo());
        verify(administradorRepository, times(1)).findById(idExistente);
        verify(administradorRepository, times(1)).save(any(Administrador.class));
    }

    @Test
    void deveAtualizarASenhaDoAdministradorComSucesso() {
        AtualizarSenhaAdministradorDTO passwordDto = new AtualizarSenhaAdministradorDTO("senha124");
        Long idExistente = 40L;

        Administrador administradorAntigo = new Administrador();

        when(passwordEncoder.encode("senha124")).thenReturn("Senhacriptografadafalsa124124");
        when(administradorRepository.findById(idExistente)).thenReturn(Optional.of(administradorAntigo));
        when(administradorRepository.save(any(Administrador.class))).thenAnswer(invocation -> invocation.getArgument(0));

        administradorAntigo.setId(idExistente);
        administradorAntigo.setSenha("Senhacriptografadafalsa124124");

        Administrador adminComSenhaAtualizada = administradorService.atualizarSenha(idExistente, passwordDto);

        assertEquals(40L, adminComSenhaAtualizada.getId());
        assertEquals("Senhacriptografadafalsa124124", adminComSenhaAtualizada.getSenha());
        verify(passwordEncoder, times(1)).encode("senha124");
        verify(administradorRepository, times(1)).findById(idExistente);
        verify(administradorRepository, times(1)).save(any(Administrador.class));
    }

    @Test
    void deveConseGuirExcluirAdministradorComSucessoPeloId() {
        Long idValido = 60L;

        when(administradorRepository.existsById(idValido)).thenReturn(true);

        administradorService.excluir(idValido);

        verify(administradorRepository, times(1)).existsById(idValido);
        verify(administradorRepository, times(1)).deleteById(idValido);
    }

    @Test
    void deveLancarExcecaoAoTentarExcluirAdministradorInexistente() {
        Long idInexistente = 999L;

        when(administradorRepository.existsById(idInexistente)).thenReturn(false);

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            administradorService.excluir(idInexistente);
        });

        assertEquals("Administrador não encontrado", excecao.getMessage());
        verify(administradorRepository, times(1)).existsById(idInexistente);
        verify(administradorRepository, never()).deleteById(idInexistente);
    }
}