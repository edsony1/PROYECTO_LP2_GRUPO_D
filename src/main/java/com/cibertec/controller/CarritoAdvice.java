package com.cibertec.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cibertec.dto.ItemCarrito;

import jakarta.servlet.http.HttpSession;

/**
 * Hace disponible la cantidad de ítems del carrito en todas las vistas
 * (usado por el ícono del carrito en el nav), sin tener que repetirlo
 * en cada controller.
 */
@ControllerAdvice
public class CarritoAdvice {

    @SuppressWarnings("unchecked")
    @ModelAttribute("totalItemsCarrito")
    public int totalItemsCarrito(HttpSession session) {

        var carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null) {
            return 0;
        }

        return carrito.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
}
