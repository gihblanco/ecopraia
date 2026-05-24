package com.project.ecopraia.entity.dtos.lixeira;

import jakarta.validation.constraints.NotBlank;

public class AtualizarLixeiraDTO {
    
    @NotBlank(message = "Latitude obrigatória")
    private Double latitude;

    @NotBlank(message = "Longitude obrigatória")
    private Double longitude;

    @NotBlank(message = "Informativo obrigatório")
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
