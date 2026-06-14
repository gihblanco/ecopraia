package com.project.ecopraia.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.lixeira.AtualizarLixeiraDTO;
import com.project.ecopraia.entity.dtos.lixeira.CriarLixeiraDTO;
import com.project.ecopraia.entity.enums.TipoEvento;
import com.project.ecopraia.repository.InformativoTipoRepository;
import com.project.ecopraia.repository.LixeiraRepository;

@Service
public class LixeiraService {

    private final LixeiraRepository lixeiraRepository;

    private final InformativoTipoRepository informativoTipoRepository;

    private final HistoricoService historicoService;

    public LixeiraService(LixeiraRepository lixeiraRepository, InformativoTipoRepository informativoTipoRepository,
            HistoricoService historicoService) {
        this.lixeiraRepository = lixeiraRepository;
        this.informativoTipoRepository = informativoTipoRepository;
        this.historicoService = historicoService;
    }

    private Usuario getUsuarioAutenticado() {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return (Usuario) authentication.getPrincipal();
    }

    public Lixeira buscarPorId(Long id) {
        return lixeiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lixeira não encontrada"));
    }

    public List<Lixeira> listarTodos() {
        return lixeiraRepository.findAll();
    }

    public Lixeira criar(CriarLixeiraDTO dto) {

        Usuario usuario = getUsuarioAutenticado();

        List<InformativoTipo> informativosTipos = informativoTipoRepository.findAllById(
                dto.getInformativosTiposIds());

        Lixeira lixeira = new Lixeira();

        lixeira.setLatitude(dto.getLatitude());
        lixeira.setLongitude(dto.getLongitude());
        lixeira.setInformativosTipos(informativosTipos);
        Lixeira lixeiraSalva = lixeiraRepository.save(lixeira);

        historicoService.registrarEvento(
                usuario.getId(),
                lixeiraSalva.getId(),
                TipoEvento.CRIACAO);

        return lixeiraSalva;
    }

    public Lixeira atualizar(Long id, AtualizarLixeiraDTO dto) {

        Usuario usuario = getUsuarioAutenticado();

        Lixeira lixeira = buscarPorId(id);

        List<InformativoTipo> informativosTipos = informativoTipoRepository.findAllById(
                dto.getInformativosTiposIds());

        lixeira.setLatitude(dto.getLatitude());
        lixeira.setLongitude(dto.getLongitude());
        lixeira.setInformativosTipos(informativosTipos);

        Lixeira lixeiraSalva = lixeiraRepository.save(lixeira);

        historicoService.registrarEvento(
                usuario.getId(),
                lixeiraSalva.getId(),
                TipoEvento.EDICAO);

        return lixeiraSalva;
    }

    public void deletar(Long id) {

        Usuario usuario = getUsuarioAutenticado();

        Lixeira lixeira = buscarPorId(id);
        
        historicoService.registrarEvento(
                usuario.getId(),
                id,
                TipoEvento.EXCLUSAO);

        lixeira.getInformativosTipos().clear();

        lixeiraRepository.save(lixeira);

        lixeiraRepository.delete(lixeira);
    }
}
