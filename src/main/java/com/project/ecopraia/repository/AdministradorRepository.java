package com.project.ecopraia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecopraia.entity.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Long>{
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
