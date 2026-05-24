package com.project.ecopraia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecopraia.entity.InformativoTipo;
import com.project.ecopraia.entity.dtos.informativo.AtualizarInformativoTipoDTO;
import com.project.ecopraia.entity.dtos.informativo.CriarInformativoTipoDTO;
import com.project.ecopraia.service.InformativoTipoService;

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
        try {
            List<InformativoTipo> informativosTipo = informativoTipoService.listarTodos();
            return ResponseEntity.ok(informativosTipo);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar informativos.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            InformativoTipo informativoTipo = informativoTipoService.buscarPorId(id);
            return ResponseEntity.ok(informativoTipo);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar informativo por id.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody  CriarInformativoTipoDTO dto){
        try {
            informativoTipoService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Informativo criado com sucesso!");
        } catch (Exception ex){
            return new ResponseEntity<>("Erro ao criar informativo.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody AtualizarInformativoTipoDTO dto) {
        try {
            informativoTipoService.atualizar(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Informativo atualizado com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao atualizar informativo.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id){
        try {
            informativoTipoService.excluir(id);
            return ResponseEntity.status(HttpStatus.OK).body("Informativo excluído com sucesso!");
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao excluir informativo.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
