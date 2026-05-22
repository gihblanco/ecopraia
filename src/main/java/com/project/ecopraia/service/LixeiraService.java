package com.project.ecopraia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.dtos.lixeira.AtualizarLixeiraDTO;
import com.project.ecopraia.entity.dtos.lixeira.CriarLixeiraDTO;
import com.project.ecopraia.repository.InformativoTipoRepository;
import com.project.ecopraia.repository.LixeiraRepository;

@Service
public class LixeiraService {

    private final LixeiraRepository lixeiraRepository;

    private final InformativoTipoRepository informativoTipoRepository;

    public LixeiraService(LixeiraRepository lixeiraRepository, InformativoTipoRepository informativoTipoRepository) {
        this.lixeiraRepository = lixeiraRepository;
        this.informativoTipoRepository = informativoTipoRepository;
    }

    public List<Lixeira> listarTodos() {
        return lixeiraRepository.findAll();
    }

    public Lixeira criar(CriarLixeiraDTO dto) {
        InformativoTipo informativoTipo = informativoTipoRepository
                .findById(dto.getInformativoTipoId())
                .orElseThrow(() -> new RuntimeException("Informativo não encontrado"));

        Lixeira lixeira = new Lixeira();

        lixeira.setLatitude(dto.getLatitude());
        lixeira.setLongitude(dto.getLongitude());
        lixeira.setInformativoTipo(informativoTipo);

        return lixeiraRepository.save(lixeira);
    }

    public Lixeira atualizar(Long id, AtualizarLixeiraDTO dto) {
        Lixeira lixeira = lixeiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lixeira não encontrada"));

        InformativoTipo informativoTipo = informativoTipoRepository
                .findById(dto.getInformativoTipoId())
                .orElseThrow(() -> new RuntimeException("Informativo não encontrado"));

        lixeira.setLatitude(dto.getLatitude());
        lixeira.setLongitude(dto.getLongitude());
        lixeira.setInformativoTipo(informativoTipo);

        return lixeiraRepository.save(lixeira);
    }

    public void deletar(Long id) {
        lixeiraRepository.deleteById(id);
    }
}
