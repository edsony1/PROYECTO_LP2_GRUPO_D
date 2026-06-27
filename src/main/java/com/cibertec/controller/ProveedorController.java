package com.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.model.Proveedor;
import com.cibertec.service.ProveedorService;
import com.cibertec.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("proveedor")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping("listado")
    public String listado(Model model) {
        model.addAttribute("lstProveedores", proveedorService.getAll());
        return "proveedor/proveedor-lista";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedor/proveedor-nuevo";
    }

    @PostMapping("registrar")
    public String registrar(@ModelAttribute Proveedor proveedor,
                            Model model,
                            RedirectAttributes flash) {

        var response = proveedorService.create(proveedor);

        if (!response.success()) {
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "proveedor/proveedor-nuevo";
        }

        flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
        return "redirect:/proveedor/listado";
    }

    @GetMapping("edicion/{id}")
    public String edicion(@PathVariable Integer id, Model model) {
        model.addAttribute("proveedor", proveedorService.getOne(id));
        return "proveedor/proveedor-editar";
    }

    @PostMapping("guardar")
    public String guardar(@ModelAttribute Proveedor proveedor,
                          Model model,
                          RedirectAttributes flash) {

        var response = proveedorService.update(proveedor);

        if (!response.success()) {
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("alert", Alert.sweetAlertError(response.mensaje()));
            return "proveedor/proveedor-editar";
        }

        flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
        return "redirect:/proveedor/listado";
    }

    @PostMapping("cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
        var response = proveedorService.changeActive(id);

        flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));
        return "redirect:/proveedor/listado";
    }
}