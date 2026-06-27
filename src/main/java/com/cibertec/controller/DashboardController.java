package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cibertec.model.Rol;
import com.cibertec.service.PedidoService;
import com.cibertec.service.ProductoService;
import com.cibertec.service.ProveedorService;
import com.cibertec.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ProductoService productoService;
    private final ProveedorService proveedorService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer idRol     = (Integer) session.getAttribute("idRol");

        if (idUsuario == null){       
        	return "redirect:/login";
        }
        
        if (!Rol.ID_ADMIN.equals(idRol)) {
        	return "redirect:/inicio";
        }
        
        model.addAttribute("totalProductos",   productoService.getAllActive().size());
        model.addAttribute("totalProveedores", proveedorService.getAllActive().size());
        model.addAttribute("totalClientes",    usuarioService.getAllClientes().size());
        model.addAttribute("totalPedidos",     pedidoService.getAll().size());

        return "dashboard";
    }
}