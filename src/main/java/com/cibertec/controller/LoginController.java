package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.dto.AutenticacionFilter;
import com.cibertec.dto.RegistroFilter;
import com.cibertec.service.AutenticacionService;
import com.cibertec.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {

    private final AutenticacionService autenticacionService;

    @GetMapping
    public String login(Model model) {

        model.addAttribute("filter", new AutenticacionFilter());

        return "login";
    }

    @GetMapping("cerrar-sesion")
    public String cerrarSesion(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("iniciar-sesion")
    public String iniciarSesion(
            @ModelAttribute AutenticacionFilter filter,
            Model model,
            RedirectAttributes flash,
            HttpSession session) {

        var usuario = autenticacionService.authenticate(filter);

        if (usuario == null) {

            String mensaje =
                    Alert.sweetAlertError("Correo y/o contraseña incorrectos");

            model.addAttribute("alert", mensaje);
            model.addAttribute("filter", filter);

            return "login";
        }

        if (!usuario.getEstado()) {

            var mensaje = Alert.sweetAlertError(
                    "La cuenta se encuentra inactiva");

            model.addAttribute("alert", mensaje);
            model.addAttribute("filter", filter);

            return "login";
        }

        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("fullName", usuario.getFullName());
        session.setAttribute("idRol", usuario.getRol().getIdRol());

        String alert = Alert.sweetAlertSuccess(
                "Bienvenido " + usuario.getFullName()
        );

        flash.addFlashAttribute("alert", alert);

        if (usuario.getRol().isAdmin()) {
            return "redirect:/dashboard";
        }

        return "redirect:/inicio";
    }

    @GetMapping("registro")
    public String registro(Model model) {

        model.addAttribute("filter", new RegistroFilter());

        return "registro";
    }

    @PostMapping("registro")
    public String registrar(
            @ModelAttribute RegistroFilter filter,
            Model model,
            RedirectAttributes flash) {

        var resultado = autenticacionService.registrar(filter);

        if (!resultado.success()) {

            String mensaje = Alert.sweetAlertError(resultado.mensaje());

            model.addAttribute("alert", mensaje);
            model.addAttribute("filter", filter);

            return "registro";
        }

        String alert = Alert.sweetAlertSuccess(
                "Cuenta creada. Ya puedes iniciar sesión"
        );

        flash.addFlashAttribute("alert", alert);

        return "redirect:/login";
    }
}
