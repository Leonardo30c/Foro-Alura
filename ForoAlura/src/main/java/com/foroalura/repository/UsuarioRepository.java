package com.foroalura.repository;

import com.foroalura.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email.
     *
     * @param email el email del usuario a buscar
     * @return Optional con el usuario si existe, vacío si no
     */
    Optional<Usuario> findByEmail(String email);
}
