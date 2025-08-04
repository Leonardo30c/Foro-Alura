package com.foroalura.controller;

import com.foroalura.dto.TopicoDTO;
import com.foroalura.model.Topico;
import com.foroalura.service.TopicoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoService topicoService;

    @Autowired
    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @GetMapping
    public ResponseEntity<Page<TopicoDTO>> listarTopicos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        Page<Topico> topicos = topicoService.listarTopicos(pageable);

        Page<TopicoDTO> dtoPage = topicos.map(t -> new TopicoDTO(
                t.getId(),
                t.getTitulo(),
                t.getMensaje(),
                t.getCurso(),
                t.getFechaCreacion(),
                t.getCreador().getEmail()
        ));

        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> registrarTopico(
            @RequestBody @Valid Topico topico,
            Authentication authentication,
            UriComponentsBuilder uriComponentsBuilder) {

        String email = authentication.getName();
        Topico topicoCreado = topicoService.crearTopico(topico, email);

        Map<String, Object> response = new HashMap<>();
        response.put("id", topicoCreado.getId());
        response.put("titulo", topicoCreado.getTitulo());
        response.put("mensaje", topicoCreado.getMensaje());
        response.put("curso", topicoCreado.getCurso());
        response.put("fechaCreacion", topicoCreado.getFechaCreacion());

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topicoCreado.getId()).toUri();
        return ResponseEntity.created(url).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerTopico(@PathVariable Long id) {
        Topico topico = topicoService.listarTodosTopicos().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", topico.getId());
        response.put("titulo", topico.getTitulo());
        response.put("mensaje", topico.getMensaje());
        response.put("curso", topico.getCurso());
        response.put("fechaCreacion", topico.getFechaCreacion());
        response.put("autor", topico.getCreador().getNombre());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        topicoService.eliminarTopico(id, email);
        return ResponseEntity.noContent().build();
    }
}
