package com.project.ecopraia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.dtos.informativo.AtualizarInformativoTipoDTO;
import com.project.ecopraia.entity.dtos.informativo.CriarInformativoTipoDTO;
import com.project.ecopraia.service.InformativoTipoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/informativos")
public class InformativoTipoController {
    
    private final InformativoTipoService informativoTipoService;

    public InformativoTipoController(InformativoTipoService informativoTipoService){
        this.informativoTipoService = informativoTipoService;
    }
    
    @GetMapping("/todos")
    public ResponseEntity<?> listarTodos(){
        List<InformativoTipo> informativosTipo = informativoTipoService.listarTodos();
        return ResponseEntity.ok(informativosTipo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        InformativoTipo informativoTipo = informativoTipoService.buscarPorId(id);
        return ResponseEntity.ok(informativoTipo);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criar(@Valid @RequestBody CriarInformativoTipoDTO dto){
        informativoTipoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Informativo criado com sucesso!");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarInformativoTipoDTO dto) {
        informativoTipoService.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Informativo atualizado com sucesso!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> excluir(@PathVariable Long id){
        informativoTipoService.excluir(id);
        return ResponseEntity.status(HttpStatus.OK).body("Informativo excluído com sucesso!");
    }

}
