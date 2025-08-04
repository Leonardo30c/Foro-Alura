package com.foroalura.service;

import com.foroalura.model.Topico;
import com.foroalura.model.Usuario;
import com.foroalura.repository.TopicoRepository;
import com.foroalura.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;

    public TopicoService(TopicoRepository topicoRepository, UsuarioRepository usuarioRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Page<Topico> listarTopicos(Pageable pageable) {
        return topicoRepository.findAllByOrderByFechaCreacionDesc(pageable);
    }

    public List<Topico> listarTodosTopicos() {
        return topicoRepository.findAllByOrderByFechaCreacionDesc();
    }

    public Topico crearTopico(Topico topico, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        topico.setCreador(usuario);
        topico.setFechaCreacion(LocalDateTime.now());
        return topicoRepository.save(topico);
    }

    public void eliminarTopico(Long id, String emailUsuario) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));

        if (!topico.getCreador().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("No tienes permiso para eliminar este tópico");
        }

        topicoRepository.delete(topico);
    }
}