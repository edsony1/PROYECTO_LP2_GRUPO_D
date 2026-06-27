package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.model.Rol;
import com.cibertec.service.UsuarioService;
import com.cibertec.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private String validarAdmin(HttpSession session) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer idRol     = (Integer) session.getAttribute("idRol");
        if (idUsuario == null)          return "redirect:/login";
        if (!Rol.ID_ADMIN.equals(idRol)) return "redirect:/inicio";
        return null;
    }

    private String validarLogueado(HttpSession session) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) return "redirect:/login";
        return null;
    }


    @GetMapping("listado")
    public String listado(Model model, HttpSession session) {
        String redirect = validarAdmin(session);
        if (redirect != null) return redirect;

        model.addAttribute("usuarios", usuarioService.getAllClientes());
        return "usuario/usuario-lista";
    }

    @GetMapping("activar/{id}")
    public String activar(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        String redirect = validarAdmin(session);
        if (redirect != null) return redirect;

        var resultado = usuarioService.activar(id);
        flash.addFlashAttribute("toast",
                Alert.sweetToast(resultado.mensaje(), resultado.success() ? "success" : "error", 4000));
        return "redirect:/usuario/listado";
    }

    @GetMapping("desactivar/{id}")
    public String desactivar(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        String redirect = validarAdmin(session);
        if (redirect != null) return redirect;

        var resultado = usuarioService.desactivar(id);
        flash.addFlashAttribute("toast",
                Alert.sweetToast(resultado.mensaje(), resultado.success() ? "success" : "error", 4000));
        return "redirect:/usuario/listado";
    }


    @GetMapping("perfil")
    public String perfil(Model model, HttpSession session) {
        String redirect = validarLogueado(session);
        if (redirect != null) return redirect;

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        model.addAttribute("usuario", usuarioService.getOne(idUsuario));
        return "usuario/perfil";
    }

    @PostMapping("perfil/guardar")
    public String guardarPerfil(
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam(required = false, defaultValue = "") String passwordActual,
            @RequestParam(required = false, defaultValue = "") String passwordNueva,
            @RequestParam(required = false, defaultValue = "") String passwordConfirmar,
            HttpSession session,
            RedirectAttributes flash) {

        String redirect = validarLogueado(session);
        if (redirect != null) return redirect;

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        var resultado = usuarioService.updatePerfil(idUsuario, nombres, apellidos, correo,
                passwordActual, passwordNueva, passwordConfirmar);

        if (resultado.success()) {
            session.setAttribute("fullName", nombres + " " + apellidos);
            flash.addFlashAttribute("toast",
                    Alert.sweetToast(resultado.mensaje(), "success", 4000));
        } else {
            flash.addFlashAttribute("toast",
                    Alert.sweetToast(resultado.mensaje(), "error", 4000));
        }

        return "redirect:/usuario/perfil";
    }
}