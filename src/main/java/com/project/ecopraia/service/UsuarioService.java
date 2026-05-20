package com.project.ecopraia.service;

import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
        return usuario;
    }
}
