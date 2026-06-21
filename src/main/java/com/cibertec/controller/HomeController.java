package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.service.CategoriaService;
import com.cibertec.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping("/")
    public String raiz() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String tienda(
            @RequestParam(required = false) Integer idCategoria,
            Model model) {

        var productos = (idCategoria == null
                ? productoService.getAll()
                : productoService.getByCategoria(idCategoria))
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.getEstado()))
                .toList();

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.getAll());
        model.addAttribute("idCategoria", idCategoria);

        return "inicio";
    }

}
