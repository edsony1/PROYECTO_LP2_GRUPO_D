package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cibertec.model.Rol;
import com.cibertec.service.PedidoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("mis-pedidos")
    public String misPedidos(Model model, HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer idRol = (Integer) session.getAttribute("idRol");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        if (Rol.ID_ADMIN.equals(idRol)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("pedidos", pedidoService.getByUsuario(idUsuario));

        return "mis-pedidos";
    }
}
