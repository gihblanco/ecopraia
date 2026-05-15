package com.project.ecopraia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecopraia.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
