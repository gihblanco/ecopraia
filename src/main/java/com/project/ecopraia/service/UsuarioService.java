package com.project.ecopraia.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.dtos.usuario.AtualizarSenhaUsuarioDTO;
import com.project.ecopraia.entity.dtos.usuario.AtualizarUsuarioDTO;
import com.project.ecopraia.entity.dtos.usuario.CriarUsuarioDTO;
import com.project.ecopraia.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario criar(CriarUsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(
                passwordEncoder.encode(dto.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, AtualizarUsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);

        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new RuntimeException("Email já cadastrado");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarSenha(Long id, AtualizarSenhaUsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.setSenha(
                passwordEncoder.encode(dto.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }
}
