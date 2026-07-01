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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.dtos.lixeira.AtualizarLixeiraDTO;
import com.project.ecopraia.entity.dtos.lixeira.CriarLixeiraDTO;
import com.project.ecopraia.entity.enums.ModoDeslocamento;
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
        List<Lixeira> lixeiras = lixeiraService.listarTodos();
        return ResponseEntity.ok(lixeiras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        Lixeira lixeira = lixeiraService.buscarPorId(id);
        return ResponseEntity.ok(lixeira);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criar(@Valid @RequestBody CriarLixeiraDTO dto){
        lixeiraService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Lixeira criada com sucesso!");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarLixeiraDTO dto){
        lixeiraService.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Lixeira atualizada com sucesso!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> excluir(@PathVariable Long id){
        lixeiraService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).body("Lixeira excluída com sucesso!");
    }

    @GetMapping("/{id}/distancia")
    public ResponseEntity<?> distancia(@PathVariable Long id,
                                       @RequestParam double lat,
                                       @RequestParam double lng){
        return ResponseEntity.ok(lixeiraService.calcularDistancia(id, lat, lng));
    }

    @GetMapping("/proximas")
    public ResponseEntity<?> proximas(@RequestParam double lat,
                                      @RequestParam double lng){
        return ResponseEntity.ok(lixeiraService.listarPorProximidade(lat, lng));
    }

    @GetMapping("/{id}/rota")
    public ResponseEntity<?> rota(@PathVariable Long id,
                                  @RequestParam double lat,
                                  @RequestParam double lng,
                                  @RequestParam(defaultValue = "A_PE") ModoDeslocamento modo){
        return ResponseEntity.ok(lixeiraService.calcularRota(id, lat, lng, modo));
    }
}
