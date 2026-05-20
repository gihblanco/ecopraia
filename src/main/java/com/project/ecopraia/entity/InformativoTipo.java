package com.project.ecopraia.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @JsonIgnore
    @OneToMany(mappedBy = "informativoTipo")
    private List<Lixeira> lixeiras;

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

    public List<Lixeira> getLixeiras() {
        return lixeiras;
    }

    public void setLixeiras(List<Lixeira> lixeiras) {
        this.lixeiras = lixeiras;
    }
}
