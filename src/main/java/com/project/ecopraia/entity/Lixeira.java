package com.project.ecopraia.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lixeira")
public class Lixeira {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lixeira")
    private Long id;
    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "id_informativo_tipo")
    private InformativoTipo informativoTipo;

    @OneToMany(mappedBy = "lixeira")
    private List<Historico> historicos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude(){
        return latitude;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    public InformativoTipo getInformativoTipo() {
        return informativoTipo;
    }

    public void setInformativoTipo(InformativoTipo informativoTipo) {
        this.informativoTipo = informativoTipo;
    }

    public List<Historico> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(List<Historico> historicos) {
        this.historicos = historicos;
    }
}
