package com.project.ecopraia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.dtos.informativo.AtualizarInformativoTipoDTO;
import com.project.ecopraia.entity.dtos.informativo.CriarInformativoTipoDTO;
import com.project.ecopraia.exception.RecursoNaoEncontradoException;
import com.project.ecopraia.repository.InformativoTipoRepository;

@Service
public class InformativoTipoService {
    private final InformativoTipoRepository informativoTipoRepository;

    public InformativoTipoService(InformativoTipoRepository informativoTipoRepository){
        this.informativoTipoRepository = informativoTipoRepository;
    }

    public InformativoTipo buscarPorId(Long id) {
        return informativoTipoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Informativo não encontrado"));
    }

    public List<InformativoTipo> listarTodos(){
        return informativoTipoRepository.findAll();
    }

    public InformativoTipo criar(CriarInformativoTipoDTO dto){
        InformativoTipo informativoTipo = new InformativoTipo();
        informativoTipo.setNomeTipo(dto.getNomeTipo());
        informativoTipo.setInformativo(dto.getInformativo());
        informativoTipo.setCor(dto.getCor());

        return informativoTipoRepository.save(informativoTipo);
    }

    public InformativoTipo atualizar(Long id, AtualizarInformativoTipoDTO dto){
        InformativoTipo informativoTipo = buscarPorId(id);

        informativoTipo.setNomeTipo(dto.getNomeTipo());
        informativoTipo.setInformativo(dto.getInformativo());
        informativoTipo.setCor(dto.getCor());

        return informativoTipoRepository.save(informativoTipo);
    }

    public void excluir(Long id){
        if (!informativoTipoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Informativo não encontrado");
        }
        informativoTipoRepository.deleteById(id);
    }
}
