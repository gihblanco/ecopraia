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
        try {
            Administrador administrador = administradorService.buscarPorId(id);
            return ResponseEntity.ok(administrador);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar usuário por id.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody CriarAdministradorDTO dto) {
        try {
            administradorService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar usuário por id.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody AtualizarAdministradorDTO dto) {
        try {
            administradorService.atualizar(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar usuário por id.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/atualizarSenha/{id}")
    public ResponseEntity<?> atualizarSenha(@PathVariable Long id, @RequestBody AtualizarSenhaAdministradorDTO dto) {
        try {
            administradorService.atualizarSenha(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Senha atualizada com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao atualizar a senha.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id){
        try{
            administradorService.excluir(id);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário excluído com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao excluir usuário.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
