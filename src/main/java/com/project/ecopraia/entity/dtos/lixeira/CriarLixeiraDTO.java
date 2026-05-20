package com.project.ecopraia.entity.dtos.lixeira;

public class CriarLixeiraDTO {
    
    private Double latitude;
    private Double longitude;

    private Long informativoTipoId;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getInformativoTipoId() {
        return informativoTipoId;
    }

    public void setInformativoTipoId(Long informativoTipoId) {
        this.informativoTipoId = informativoTipoId;
    }
    
}
