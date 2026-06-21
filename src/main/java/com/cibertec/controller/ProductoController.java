package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.model.Producto;
import com.cibertec.model.Rol;
import com.cibertec.repository.CategoriaRepository;
import com.cibertec.service.CategoriaService;
import com.cibertec.service.ProductoService;
import com.cibertec.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final CategoriaRepository categoriaRepository;

    private String validarAcceso(HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer idRol = (Integer) session.getAttribute("idRol");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        if (!Rol.ID_ADMIN.equals(idRol)) {
            return "redirect:/inicio";
        }

        return null;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        model.addAttribute("productos", productoService.getAll());

        return "producto-lista";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model, HttpSession session) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.getAll());

        return "producto-form";
    }

    @GetMapping("editar/{idProd}")
    public String editar(@PathVariable String idProd, Model model, HttpSession session) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        model.addAttribute("producto", productoService.getOne(idProd));
        model.addAttribute("categorias", categoriaService.getAll());

        return "producto-form";
    }

    @PostMapping("guardar")
    public String guardar(
            @ModelAttribute Producto producto,
            @RequestParam Integer idCategoria,
            HttpSession session,
            RedirectAttributes flash) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        var categoria = categoriaRepository.findById(idCategoria).orElseThrow();
        producto.setCategoria(categoria);

        var esNuevo = producto.getIdProd() == null || producto.getIdProd().isBlank();

        if (!esNuevo) {
            var actual = productoService.getOne(producto.getIdProd());
            producto.setEstado(actual.getEstado());
        }

        var resultado = esNuevo
                ? productoService.create(producto)
                : productoService.update(producto);

        String alert = resultado.success()
                ? Alert.sweetAlertSuccess(resultado.mensaje())
                : Alert.sweetAlertError(resultado.mensaje());

        flash.addFlashAttribute("alert", alert);

        return "redirect:/producto";
    }

    @GetMapping("desactivar/{idProd}")
    public String desactivar(@PathVariable String idProd, HttpSession session, RedirectAttributes flash) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        var resultado = productoService.desactivar(idProd);

        String alert = resultado.success()
                ? Alert.sweetAlertSuccess(resultado.mensaje())
                : Alert.sweetAlertError(resultado.mensaje());

        flash.addFlashAttribute("alert", alert);

        return "redirect:/producto";
    }

    @GetMapping("activar/{idProd}")
    public String activar(@PathVariable String idProd, HttpSession session, RedirectAttributes flash) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        var resultado = productoService.activar(idProd);

        String alert = resultado.success()
                ? Alert.sweetAlertSuccess(resultado.mensaje())
                : Alert.sweetAlertError(resultado.mensaje());

        flash.addFlashAttribute("alert", alert);

        return "redirect:/producto";
    }
}
