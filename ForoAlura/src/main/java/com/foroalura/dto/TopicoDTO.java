package com.foroalura.dto;

import java.time.LocalDateTime;

public class TopicoDTO {

    private Long id;
    private String titulo;
    private String mensaje;
    private String curso;
    private LocalDateTime fechaCreacion;
    private String creadorEmail;

    public TopicoDTO(Long id, String titulo, String mensaje, String curso, LocalDateTime fechaCreacion, String creadorEmail) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.curso = curso;
        this.fechaCreacion = fechaCreacion;
        this.creadorEmail = creadorEmail;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCreadorEmail() {
        return creadorEmail;
    }

    public void setCreadorEmail(String creadorEmail) {
        this.creadorEmail = creadorEmail;
    }
}
