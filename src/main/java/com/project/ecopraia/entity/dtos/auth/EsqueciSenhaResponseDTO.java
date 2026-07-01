package com.project.ecopraia.entity.dtos.auth;

public class EsqueciSenhaResponseDTO {

    private String token;

    public EsqueciSenhaResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
