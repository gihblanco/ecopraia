package com.project.ecopraia.entity.dtos.lixeira;

public class LixeiraDistanciaDTO {

    private Long idLixeira;
    private Double latitude;
    private Double longitude;
    private double distanciaMetros;

    public LixeiraDistanciaDTO(Long idLixeira, Double latitude, Double longitude, double distanciaMetros) {
        this.idLixeira = idLixeira;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanciaMetros = distanciaMetros;
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

    public double getDistanciaMetros() {
        return distanciaMetros;
    }
}
