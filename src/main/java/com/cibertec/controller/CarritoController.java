package com.cibertec.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.dto.ItemCarrito;
import com.cibertec.model.Rol;
import com.cibertec.service.PedidoService;
import com.cibertec.service.ProductoService;
import com.cibertec.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("carrito")
@RequiredArgsConstructor
public class CarritoController {

    private static final String SESSION_KEY = "carrito";

    private final ProductoService productoService;
    private final PedidoService pedidoService;

    @SuppressWarnings("unchecked")
    private List<ItemCarrito> obtenerCarrito(HttpSession session) {
        var carrito = (List<ItemCarrito>) session.getAttribute(SESSION_KEY);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(SESSION_KEY, carrito);
        }
        return carrito;
    }

    /**
     * Solo los clientes pueden usar el carrito. Si no hay sesión, se manda
     * a login; si es administrador, se redirige al dashboard.
     */
    private String validarAcceso(HttpSession session) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        Integer idRol = (Integer) session.getAttribute("idRol");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        if (Rol.ID_ADMIN.equals(idRol)) {
            return "redirect:/dashboard";
        }

        return null;
    }

    @GetMapping
    public String ver(Model model, HttpSession session) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        var carrito = obtenerCarrito(session);

        double total = carrito.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);

        return "carrito";
    }

    @PostMapping("agregar")
    public String agregar(
            @RequestParam("idProd") String idProd,
            @RequestParam(value = "cantidad", defaultValue = "1") Integer cantidad,
            HttpSession session,
            RedirectAttributes flash) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Inicia sesión para agregar productos al carrito"));
            return "redirect:/login";
        }

        Integer idRol = (Integer) session.getAttribute("idRol");
        if (Rol.ID_ADMIN.equals(idRol)) {
            return "redirect:/dashboard";
        }

        var producto = productoService.getOne(idProd);

        if (!Boolean.TRUE.equals(producto.getEstado())) {
            flash.addFlashAttribute("alert", Alert.sweetAlertError("Ese producto no está disponible"));
            return "redirect:/inicio";
        }

        var carrito = obtenerCarrito(session);

        var existente = carrito.stream()
                .filter(i -> i.getIdProd().equals(idProd))
                .findFirst();

        int cantidadEnCarrito = existente.map(ItemCarrito::getCantidad).orElse(0);

        if (producto.getStock() == null || producto.getStock() < cantidadEnCarrito + cantidad) {
            flash.addFlashAttribute("alert", Alert.sweetAlertError("No hay suficiente stock disponible"));
            return "redirect:/inicio";
        }

        if (existente.isPresent()) {
            existente.get().setCantidad(cantidadEnCarrito + cantidad);
        } else {
            var item = new ItemCarrito();
            item.setIdProd(producto.getIdProd());
            item.setNombre(producto.getNombre());
            item.setImagen(producto.getImagen());
            item.setPrecio(producto.getPrecio());
            item.setCantidad(cantidad);
            carrito.add(item);
        }

        flash.addFlashAttribute("alert", Alert.sweetToast("Agregado al carrito", "success", 1800));

        return "redirect:/inicio";
    }

    @PostMapping("actualizar")
    public String actualizar(
            @RequestParam("idProd") String idProd,
            @RequestParam("cantidad") Integer cantidad,
            HttpSession session) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        var carrito = obtenerCarrito(session);
        var producto = productoService.getOne(idProd);

        carrito.stream()
                .filter(i -> i.getIdProd().equals(idProd))
                .findFirst()
                .ifPresent(item -> {
                    int nuevaCantidad = Math.max(1, cantidad);
                    if (producto.getStock() != null && nuevaCantidad > producto.getStock()) {
                        nuevaCantidad = producto.getStock();
                    }
                    item.setCantidad(nuevaCantidad);
                });

        return "redirect:/carrito";
    }

    @PostMapping("quitar")
    public String quitar(@RequestParam("idProd") String idProd, HttpSession session) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        var carrito = obtenerCarrito(session);
        carrito.removeIf(i -> i.getIdProd().equals(idProd));

        return "redirect:/carrito";
    }

    @PostMapping("confirmar")
    public String confirmar(HttpSession session, Model model, RedirectAttributes flash) {

        String redirect = validarAcceso(session);
        if (redirect != null) {
            return redirect;
        }

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        var carrito = obtenerCarrito(session);

        var resultado = pedidoService.confirmarCompra(idUsuario, carrito);

        if (!resultado.success()) {
            model.addAttribute("alert", Alert.sweetAlertError(resultado.mensaje()));
            model.addAttribute("carrito", carrito);
            model.addAttribute("total", carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum());
            return "carrito";
        }

        session.removeAttribute(SESSION_KEY);

        flash.addFlashAttribute("alert", Alert.sweetAlertSuccess(resultado.mensaje()));

        return "redirect:/mis-pedidos";
    }
}
