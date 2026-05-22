package com.project.ecopraia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecopraia.entity.dtos.informativo.CriarInformativoTipoDTO;
import com.project.ecopraia.service.InformativoTipoService;

@RestController
@RequestMapping("/informativos")
public class InformativoTipoController {
    
    private final InformativoTipoService informativoTipoService;

    public InformativoTipoController(InformativoTipoService informativoTipoService){
        this.informativoTipoService = informativoTipoService;
    }
}
