package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.Producto;
import com.cibertec.repository.ProductoRepository;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    public List<Producto> getByCategoria(Integer idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria);
    }

    public Producto getOne(String idProd) {
        return productoRepository.findById(idProd).orElseThrow();
    }

    public ResultadoResponse create(Producto producto) {
        try {
            producto.setIdProd(generarIdProd());
            producto.setEstado(true);
            var registro = productoRepository.save(producto);
            return new ResultadoResponse(true, "Producto " + registro.getNombre() + " registrado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al registrar producto");
        }
    }

    public ResultadoResponse update(Producto producto) {
        try {
            var registro = productoRepository.save(producto);
            return new ResultadoResponse(true, "Producto " + registro.getNombre() + " actualizado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al actualizar producto");
        }
    }

    public ResultadoResponse desactivar(String idProd) {
        try {
            var producto = getOne(idProd);
            producto.setEstado(false);
            productoRepository.save(producto);
            return new ResultadoResponse(true, "Producto desactivado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al desactivar producto");
        }
    }

    public ResultadoResponse activar(String idProd) {
        try {
            var producto = getOne(idProd);
            producto.setEstado(true);
            productoRepository.save(producto);
            return new ResultadoResponse(true, "Producto activado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al activar producto");
        }
    }

    private String generarIdProd() {
        var productos = productoRepository.findAll();

        int siguiente = productos.stream()
                .map(Producto::getIdProd)
                .filter(id -> id != null && id.trim().matches("PR\\d+"))
                .map(id -> id.trim().substring(2))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        return String.format("PR%03d", siguiente);
    }
}
