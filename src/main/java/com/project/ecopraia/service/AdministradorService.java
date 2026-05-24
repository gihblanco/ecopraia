package com.project.ecopraia.service;

import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.Administrador;
import com.project.ecopraia.entity.dtos.admin.AtualizarAdministradorDTO;
import com.project.ecopraia.entity.dtos.admin.AtualizarSenhaAdministradorDTO;
import com.project.ecopraia.entity.dtos.admin.CriarAdministradorDTO;
import com.project.ecopraia.repository.AdministradorRepository;

@Service
public class AdministradorService {
    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public Administrador buscarPorId(Long id) {
        return administradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Administrador não encontrado"));
    }

    public Administrador criar(CriarAdministradorDTO dto) {

        if (administradorRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Administrador administrador = new Administrador();

        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setSenha(dto.getSenha());
        administrador.setCpf(dto.getCpf());
        administrador.setInstituicao(dto.getInstituicao());
        administrador.setCargo(dto.getCargo());

        return administradorRepository.save(administrador);
    }

    public Administrador atualizar(Long id, AtualizarAdministradorDTO dto) {
        Administrador administrador = buscarPorId(id);

        if (administradorRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new RuntimeException("Email já cadastrado");
        }

        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setInstituicao(dto.getInstituicao());
        administrador.setCargo(dto.getCargo());
        return administradorRepository.save(administrador);
    }

    public Administrador atualizarSenha(Long id, AtualizarSenhaAdministradorDTO dto) {
        Administrador administrador = buscarPorId(id);
        administrador.setSenha(dto.getSenha());
        return administradorRepository.save(administrador);
    }

    public void excluir(Long id) {
        administradorRepository.deleteById(id);
    }
}
