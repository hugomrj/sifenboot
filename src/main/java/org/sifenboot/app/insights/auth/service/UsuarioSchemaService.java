package org.sifenboot.app.insights.auth.service;

import jakarta.transaction.Transactional;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.insights.auth.model.Usuario;
import org.sifenboot.app.insights.auth.repository.UsuarioSchemaRepository;
import org.sifenboot.errors.DuplicateEntityException;
import org.sifenboot.setup.db.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UsuarioSchemaService {

    private final DbUtils db;
    private final UsuarioSchemaRepository usuarioSchemaRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioSchemaService(
            DbUtils db,
            UsuarioSchemaRepository usuarioSchemaRepository,
            PasswordEncoder passwordEncoder) {

        this.db = db;
        this.usuarioSchemaRepository = usuarioSchemaRepository;
        this.passwordEncoder = passwordEncoder;
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

            usuario.setPassword(
                    passwordEncoder.encode(usuario.getPassword())
            );

            usuarioSchemaRepository.save(usuario);

        } catch (DataIntegrityViolationException ex) {

            throw new DuplicateEntityException(
                    "Ya existe un usuario con ese nombre."
            );

        }

        db.setSchema("public");
    }



}