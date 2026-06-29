package com.project.ecopraia.entity.enums;

public enum ModoDeslocamento {

    A_PE("foot-walking", "A pé"),
    CARRO("driving-car", "De carro"),
    BICICLETA("cycling-regular", "De bicicleta");

    private final String perfilOrs;

    private final String descricao;

    ModoDeslocamento(String perfilOrs, String descricao) {
        this.perfilOrs = perfilOrs;
        this.descricao = descricao;
    }

    public String getPerfilOrs() {
        return perfilOrs;
    }

    public String getDescricao() {
        return descricao;
    }
}
