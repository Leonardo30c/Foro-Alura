package com.foroalura.repository;

import com.foroalura.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad Topico.
 */
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    /**
     * Obtiene una página de tópicos ordenados por fecha de creación descendente.
     *
     * @param pageable configuración de paginación y ordenamiento
     * @return página de tópicos ordenados por fecha creación descendente
     */
    Page<Topico> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    /**
     * Obtiene todos los tópicos ordenados por fecha de creación descendente.
     *
     * @return lista de tópicos ordenados por fecha creación descendente
     */
    List<Topico> findAllByOrderByFechaCreacionDesc();
}
