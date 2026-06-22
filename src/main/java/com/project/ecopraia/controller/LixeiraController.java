package com.project.ecopraia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.dtos.lixeira.AtualizarLixeiraDTO;
import com.project.ecopraia.entity.dtos.lixeira.CriarLixeiraDTO;
import com.project.ecopraia.service.LixeiraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/lixeiras")
public class LixeiraController {
    
    private final LixeiraService lixeiraService;

    public LixeiraController(LixeiraService lixeiraService){
        this.lixeiraService = lixeiraService;
    }

    @GetMapping("/todos")
    public ResponseEntity<?> listarTodos(){
        try {
            List<Lixeira> lixeiras = lixeiraService.listarTodos();
            return ResponseEntity.ok(lixeiras);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar lixeiras", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            Lixeira lixeira = lixeiraService.buscarPorId(id);
            return ResponseEntity.ok(lixeira);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar lixeira por id.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criar(@Valid @RequestBody CriarLixeiraDTO dto){
        try {
            lixeiraService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Lixeira criada com sucesso!");
        } catch(Exception ex){
            return new ResponseEntity<>("Erro ao criar lixeira.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarLixeiraDTO dto){
        try {
            lixeiraService.atualizar(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Lixeira atualizada com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao atualizar lixeira.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> excluir(@PathVariable Long id){
        try {
            lixeiraService.deletar(id);
            return ResponseEntity.status(HttpStatus.OK).body("Lixeira excluída com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao excluir lixeira.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
