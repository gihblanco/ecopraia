package com.project.ecopraia.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.ecopraia.entity.Historico;
import com.project.ecopraia.entity.Lixeira;
import com.project.ecopraia.entity.Usuario;
import com.project.ecopraia.entity.enums.TipoEvento;
import com.project.ecopraia.repository.HistoricoRepository;
import com.project.ecopraia.repository.LixeiraRepository;
import com.project.ecopraia.repository.UsuarioRepository;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    private final UsuarioRepository usuarioRepository;

    private final LixeiraRepository lixeiraRepository;

    public HistoricoService(HistoricoRepository historicoRepository, UsuarioRepository usuarioRepository,
            LixeiraRepository lixeiraRepository) {
        this.historicoRepository = historicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.lixeiraRepository = lixeiraRepository;
    }

    public List<Historico> listarTodos(){
        return historicoRepository.findAll();
    }

    public Historico buscarPorId(Long id) {
        return historicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Historico não encontrado"));
    }

    public Historico registrarEvento(Long usuarioId, Long lixeiraId, TipoEvento tipoEvento) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Lixeira lixeira = lixeiraRepository.findById(lixeiraId)
                .orElseThrow(() -> new RuntimeException("Lixeira não encontrada"));

        Historico historico = new Historico();

        historico.setUsuario(usuario);
        historico.setLixeira(lixeira);
        historico.setTipoEvento(tipoEvento);
        historico.setDataHora(LocalDateTime.now());

        return historicoRepository.save(historico);
    }

    public void excluir(Long id) {
        historicoRepository.deleteById(id);
    }
}
