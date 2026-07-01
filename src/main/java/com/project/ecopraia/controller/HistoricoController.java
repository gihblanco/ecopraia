package com.project.ecopraia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecopraia.entity.Historico;
import com.project.ecopraia.entity.enums.TipoEvento;
import com.project.ecopraia.service.HistoricoService;

@RestController
@RequestMapping("/historicos")
public class HistoricoController {

    private final HistoricoService historicoService;

    public HistoricoController(HistoricoService historicoService){
        this.historicoService = historicoService;
    }
    
    @GetMapping("/todos")
    public ResponseEntity<?> listarTodos(){
        List<Historico> historicos = historicoService.listarTodos();
        return ResponseEntity.ok(historicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        Historico historico = historicoService.buscarPorId(id);
        return ResponseEntity.ok(historico);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestParam Long usuarioId, @RequestParam Long lixeiraId, @RequestParam TipoEvento tipoEvento){
        historicoService.registrarEvento(usuarioId, lixeiraId, tipoEvento);
        return ResponseEntity.status(HttpStatus.CREATED).body("Histórico registrado com sucesso!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id){
        historicoService.excluir(id);
        return ResponseEntity.status(HttpStatus.OK).body("Histórico excluído com sucesso!");
    }
}
