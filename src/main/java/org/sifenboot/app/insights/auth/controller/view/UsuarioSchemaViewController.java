package org.sifenboot.app.insights.auth.controller.view;

import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.service.EmisorService;
import org.sifenboot.app.insights.auth.model.Usuario;
import org.sifenboot.app.insights.auth.service.UsuarioSchemaService;
import org.sifenboot.errors.DuplicateEntityException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/app/schema/usuarios/")
public class UsuarioSchemaViewController {

    private final EmisorService emisorService;
    private final UsuarioSchemaService usuarioSchemaService;

    public UsuarioSchemaViewController(EmisorService emisorService,
                                       UsuarioSchemaService usuarioSchemaService) {
        this.emisorService = emisorService;
        this.usuarioSchemaService = usuarioSchemaService;
    }

    @GetMapping("/view")
    public String view(
            @RequestParam(required = false) Integer emisorId,
            Model model) {

        model.addAttribute("emisores", emisorService.findAll());
        model.addAttribute("titulo", "Gestión de Usuarios");
        model.addAttribute("emisorId", emisorId);

        if (emisorId != null) {

            Emisor emisor = emisorService.findById(emisorId);

            model.addAttribute(
                    "usuarios",
                    usuarioSchemaService.findAll(emisor.getCodEmisor())
            );
        }

        return "ui/usuarioshema/view";
    }

    @GetMapping("/list")
    public String list(
            @RequestParam("emisorId") Integer emisorId,
            Model model) {

        Emisor emisor = emisorService.findById(emisorId);
        List<Usuario> usuarios = usuarioSchemaService.findAll(emisor.getCodEmisor());

        // obtener esquema desde emisorId
        // buscar usuarios

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("emisorId", emisorId);

        return "ui/usuarioshema/list";
    }


    @GetMapping("/form")
    public String form(
            @RequestParam Integer emisorId,
            Model model) {

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("emisorId", emisorId);
        model.addAttribute("titulo", "Nuevo Usuario");
        model.addAttribute("isNew", true);

        return "ui/usuarioshema/form";
    }



    @PostMapping("/save")
    public String save(
            @RequestParam Integer emisorId,
            @ModelAttribute Usuario usuario,
            Model model) {

        try {

            Emisor emisor = emisorService.findById(emisorId);

            usuarioSchemaService.crearUsuario(
                    emisor.getCodEmisor(),
                    usuario
            );

            return view(emisorId, model);

        } catch (DuplicateEntityException ex) {

            model.addAttribute("titulo", "Nuevo Usuario");
            model.addAttribute("usuario", usuario);
            model.addAttribute("emisorId", emisorId);
            model.addAttribute("isNew", true);
            model.addAttribute("errorMessage", ex.getMessage());

            return "ui/usuarioshema/form";
        }
    }






}