package com.project.ecopraia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "informativo_tipo")
public class InformativoTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informativo_tipo")
    private Long id;

    @Column(name = "nome_tipo")
    private String nomeTipo;

    private String informativo;

    private String cor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTipo(){
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo){
        this.nomeTipo = nomeTipo;
    }

    public String getInformativo(){
        return informativo;
    }

    public void setInformativo(String informativo){
        this.informativo = informativo;
    }

    public String getCor(){
        return cor;
    }
    
    public void setCor(String cor){
        this.cor = cor;
    }
}
