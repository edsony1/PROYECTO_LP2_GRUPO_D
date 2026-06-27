package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.Proveedor;
import com.cibertec.repository.ProveedorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public List<Proveedor> getAll() {
        return proveedorRepository.findAllByOrderByIdProveedorDesc();
    }

    public List<Proveedor> getAllActive() {
        return proveedorRepository.findAllByEstadoTrue();
    }

    public ResultadoResponse create(Proveedor proveedor) {
        try {
            var registro = proveedorRepository.save(proveedor);
            var mensaje = String.format("Proveedor con Id %s registrado", registro.getIdProveedor());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    public Proveedor getOne(Integer idProveedor) {
        return proveedorRepository.findById(idProveedor).orElseThrow();
    }

    public ResultadoResponse update(Proveedor proveedor) {
        try {
            var registro = proveedorRepository.save(proveedor);
            var mensaje = String.format("Proveedor con Id %s actualizado", registro.getIdProveedor());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    public ResultadoResponse delete(Integer idProveedor) {
        try {
            proveedorRepository.deleteById(idProveedor);
            var mensaje = String.format("Proveedor con Id %s eliminado", idProveedor);
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    @Transactional
    public ResultadoResponse changeActive(Integer idProveedor) {
        var proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedor.setEstado(!proveedor.isEstado()); // alterna true/false
        proveedorRepository.save(proveedor);

        var estado = proveedor.isEstado() ? "activado" : "desactivado";
        var mensaje = String.format("Proveedor con Id %s %s", proveedor.getIdProveedor(), estado);

        return new ResultadoResponse(true, mensaje);
    }
}
