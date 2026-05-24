package com.project.ecopraia.entity.dtos.informativo;

import jakarta.validation.constraints.NotBlank;

public class CriarInformativoTipoDTO {
    
    @NotBlank(message = "Obrigatório o nome do tipo")
    private String nomeTipo;

    @NotBlank(message = "Obrigatória a descrição do informativo")
    private String informativo;

    @NotBlank(message = "Obrigatória a cor do tipo")
    private String cor;

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public String getInformativo() {
        return informativo;
    }

    public void setInformativo(String informativo) {
        this.informativo = informativo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
