package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cibertec.model.Rol;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer idRol = (Integer) session.getAttribute("idRol");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        if (!Rol.ID_ADMIN.equals(idRol)) {
            return "redirect:/inicio";
        }

        return "dashboard";
    }

}
