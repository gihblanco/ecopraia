package com.project.ecopraia.entity.dtos.lixeira;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class CriarLixeiraDTO {

    @NotNull(message = "Latitude obrigatória")
    private Double latitude;

    @NotNull(message = "Longitude obrigatória")
    private Double longitude;

    @NotNull(message = "Informativo obrigatório")
    private List<Long> informativosTiposIds;

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

    public List<Long> getInformativosTiposIds() {
        return informativosTiposIds;
    }

    public void setInformativosTiposIds(List<Long> informativosTiposIds) {
        this.informativosTiposIds = informativosTiposIds;
    }

    public CriarLixeiraDTO(@NotNull(message = "Latitude obrigatória") Double latitude,
            @NotNull(message = "Longitude obrigatória") Double longitude,
            @NotNull(message = "Informativo obrigatório") List<Long> informativosTiposIds) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.informativosTiposIds = informativosTiposIds;
    }
}