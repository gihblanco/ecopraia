package com.project.ecopraia.entity.dtos.lixeira;

import com.project.ecopraia.entity.enums.ModoDeslocamento;

public class RotaLixeiraDTO {

    private Long idLixeira;
    private Double latitude;
    private Double longitude;

    private double distanciaLinhaRetaMetros;
    private double distanciaRotaMetros;
    private double duracaoSegundos;
    private String duracaoFormatada;
    private ModoDeslocamento modo;
    private String modoDescricao;

    public RotaLixeiraDTO(Long idLixeira, Double latitude, Double longitude,
            double distanciaLinhaRetaMetros, double distanciaRotaMetros,
            double duracaoSegundos, String duracaoFormatada, ModoDeslocamento modo) {
        this.idLixeira = idLixeira;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanciaLinhaRetaMetros = distanciaLinhaRetaMetros;
        this.distanciaRotaMetros = distanciaRotaMetros;
        this.duracaoSegundos = duracaoSegundos;
        this.duracaoFormatada = duracaoFormatada;
        this.modo = modo;
        this.modoDescricao = modo.getDescricao();
    }

    public Long getIdLixeira() {
        return idLixeira;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public double getDistanciaLinhaRetaMetros() {
        return distanciaLinhaRetaMetros;
    }

    public double getDistanciaRotaMetros() {
        return distanciaRotaMetros;
    }

    public double getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public String getDuracaoFormatada() {
        return duracaoFormatada;
    }

    public ModoDeslocamento getModo() {
        return modo;
    }

    public String getModoDescricao() {
        return modoDescricao;
    }
}
