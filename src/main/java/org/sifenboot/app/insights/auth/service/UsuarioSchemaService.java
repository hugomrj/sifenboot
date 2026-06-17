package org.sifenboot.app.insights.auth.service;

import jakarta.transaction.Transactional;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.insights.auth.model.Usuario;
import org.sifenboot.app.insights.auth.repository.UsuarioSchemaRepository;
import org.sifenboot.errors.DuplicateEntityException;
import org.sifenboot.setup.db.DbUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UsuarioSchemaService {

    private final DbUtils db;
    private final UsuarioSchemaRepository usuarioSchemaRepository;

    public UsuarioSchemaService(
            DbUtils db,
            UsuarioSchemaRepository usuarioSchemaRepository) {

        this.db = db;
        this.usuarioSchemaRepository = usuarioSchemaRepository;
    }

    @Transactional
    public List<Usuario> findAll(String codEmisor) {

        try {

            db.setSchema(codEmisor);

            return usuarioSchemaRepository.findAll();

        } finally {

            db.setSchema("public");

        }
    }




    @Transactional
    public void crearUsuario(String codEmisor, Usuario usuario) {

        db.setSchema(codEmisor);

        try {

            usuarioSchemaRepository.saveAndFlush(usuario);

        } catch (DataIntegrityViolationException ex) {

            throw new DuplicateEntityException(
                    "Ya existe un usuario con ese nombre."
            );
        }

        db.setSchema("public");
    }


}