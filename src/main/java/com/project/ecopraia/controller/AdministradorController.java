package com.project.ecopraia.controller;

import com.project.ecopraia.repository.AdministradorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecopraia.entity.Administrador;
import com.project.ecopraia.entity.dtos.admin.AtualizarAdministradorDTO;
import com.project.ecopraia.entity.dtos.admin.AtualizarSenhaAdministradorDTO;
import com.project.ecopraia.entity.dtos.admin.CriarAdministradorDTO;
import com.project.ecopraia.service.AdministradorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService,
            AdministradorRepository administradorRepository) {
        this.administradorService = administradorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Administrador administrador = administradorService.buscarPorId(id);
        return ResponseEntity.ok(administrador);
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody CriarAdministradorDTO dto) {
        administradorService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Administrador criado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarAdministradorDTO dto) {
        administradorService.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Administrador atualizado com sucesso!");
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<?> atualizarSenha(@PathVariable Long id, @Valid @RequestBody AtualizarSenhaAdministradorDTO dto) {
        administradorService.atualizarSenha(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Senha atualizada com sucesso!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id){
        administradorService.excluir(id);
        return ResponseEntity.status(HttpStatus.OK).body("Administrador excluído com sucesso!");
    }

}
