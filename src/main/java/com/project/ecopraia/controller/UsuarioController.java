package com.project.ecopraia.controller;

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

import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.usuario.AtualizarSenhaUsuarioDTO;
import com.project.ecopraia.entity.dtos.usuario.AtualizarUsuarioDTO;
import com.project.ecopraia.entity.dtos.usuario.CriarUsuarioDTO;
import com.project.ecopraia.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar usuário por id.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody CriarUsuarioDTO dto) {
        try {
            usuarioService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao criar usuário.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarUsuarioDTO dto) {
        try {
            usuarioService.atualizar(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao atualizar usuário.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<?> atualizarSenha(@PathVariable Long id, @Valid @RequestBody AtualizarSenhaUsuarioDTO dto) {
        try {
            usuarioService.atualizarSenha(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Senha alterado com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao atualizar senha.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            usuarioService.excluir(id);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário excluído com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao atualizar senha.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
