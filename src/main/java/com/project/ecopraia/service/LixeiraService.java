package com.project.ecopraia.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.lixeira.AtualizarLixeiraDTO;
import com.project.ecopraia.entity.dtos.lixeira.CriarLixeiraDTO;
import com.project.ecopraia.entity.dtos.lixeira.LixeiraDistanciaDTO;
import com.project.ecopraia.entity.dtos.lixeira.RotaLixeiraDTO;
import com.project.ecopraia.entity.enums.ModoDeslocamento;
import com.project.ecopraia.entity.enums.TipoEvento;
import com.project.ecopraia.exception.RecursoNaoEncontradoException;
import com.project.ecopraia.repository.InformativoTipoRepository;
import com.project.ecopraia.repository.LixeiraRepository;
import com.project.ecopraia.service.RotaService.RotaResultado;
import com.project.ecopraia.util.GeoUtils;

@Service
public class LixeiraService {

    private final LixeiraRepository lixeiraRepository;

    private final InformativoTipoRepository informativoTipoRepository;

    private final HistoricoService historicoService;

    private final RotaService rotaService;

    public LixeiraService(LixeiraRepository lixeiraRepository, InformativoTipoRepository informativoTipoRepository,
            HistoricoService historicoService, RotaService rotaService) {
        this.lixeiraRepository = lixeiraRepository;
        this.informativoTipoRepository = informativoTipoRepository;
        this.historicoService = historicoService;
        this.rotaService = rotaService;
    }

    private Usuario getUsuarioAutenticado() {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return (Usuario) authentication.getPrincipal();
    }

    public Lixeira buscarPorId(Long id) {
        return lixeiraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lixeira não encontrada"));
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

    public LixeiraDistanciaDTO calcularDistancia(Long idLixeira, double latUsuario, double lonUsuario) {
        Lixeira lixeira = buscarPorId(idLixeira);
        double distancia = GeoUtils.distanciaEmMetros(
                latUsuario, lonUsuario, lixeira.getLatitude(), lixeira.getLongitude());
        return new LixeiraDistanciaDTO(
                lixeira.getId(), lixeira.getLatitude(), lixeira.getLongitude(), distancia);
    }

    public List<LixeiraDistanciaDTO> listarPorProximidade(double latUsuario, double lonUsuario) {
        return lixeiraRepository.findAll().stream()
                .map(l -> new LixeiraDistanciaDTO(
                        l.getId(), l.getLatitude(), l.getLongitude(),
                        GeoUtils.distanciaEmMetros(latUsuario, lonUsuario, l.getLatitude(), l.getLongitude())))
                .sorted(Comparator.comparingDouble(LixeiraDistanciaDTO::getDistanciaMetros))
                .toList();
    }

    public RotaLixeiraDTO calcularRota(Long idLixeira, double latUsuario, double lonUsuario, ModoDeslocamento modo) {
        Lixeira lixeira = buscarPorId(idLixeira);

        double linhaReta = GeoUtils.distanciaEmMetros(
                latUsuario, lonUsuario, lixeira.getLatitude(), lixeira.getLongitude());

        RotaResultado rota = rotaService.calcularRota(
                latUsuario, lonUsuario, lixeira.getLatitude(), lixeira.getLongitude(), modo);

        return new RotaLixeiraDTO(
                lixeira.getId(), lixeira.getLatitude(), lixeira.getLongitude(),
                linhaReta, rota.distanciaMetros(), rota.duracaoSegundos(),
                RotaService.formatarDuracao(rota.duracaoSegundos()), modo);
    }
}
