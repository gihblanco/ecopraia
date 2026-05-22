package com.project.ecopraia.service;

import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.usuario.AtualizarSenhaUsuarioDTO;
import com.project.ecopraia.entity.dtos.usuario.AtualizarUsuarioDTO;
import com.project.ecopraia.entity.dtos.usuario.CriarUsuarioDTO;
import com.project.ecopraia.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario criar(CriarUsuarioDTO dto){
        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, AtualizarUsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarSenha(Long id, AtualizarSenhaUsuarioDTO dto){
        Usuario usuario = buscarPorId(id);
        usuario.setSenha(dto.getSenha());
        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id){
        usuarioRepository.deleteById(id);
    }
}
