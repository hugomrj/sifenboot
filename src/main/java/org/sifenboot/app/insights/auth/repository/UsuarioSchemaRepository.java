package org.sifenboot.app.insights.auth.repository;

import org.sifenboot.app.insights.auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioSchemaRepository
        extends JpaRepository<Usuario, Long> {

}