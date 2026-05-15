package com.project.ecopraia.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrador")
public class Administrador extends Usuario {    

    private String cpf;
    private String instituicao;
    private String cargo;

    public String getCpf(){
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getInstituicao(){
        return instituicao;
    }

    public void setInstituicao(String instituicao){
        this.instituicao = instituicao;
    }

    public String getCargo(){
        return cargo;
    }

    public void setCargo(String cargo){
        this.cargo = cargo;
    }
}
