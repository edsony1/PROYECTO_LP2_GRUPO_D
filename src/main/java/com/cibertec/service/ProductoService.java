package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cibertec.dto.ProductoFilter;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.Producto;
import com.cibertec.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> getAll() {
        return productoRepository.findAllByOrderByIdProdDesc();
    }

    public List<Producto> getAllActive() {
        return productoRepository.findAllByEstadoTrue();
    }

    public List<Producto> getByCategoria(Integer idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> search(ProductoFilter filter) {
        return productoRepository.findAllByFilters(filter.getIdCategoria(), filter.getIdProveedor());
    }

    public ResultadoResponse create(Producto producto) {
        try {
            var registro = productoRepository.save(producto);
            var mensaje = String.format("Producto con Id %s registrado", registro.getIdProd());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    public Producto getOne(String idProd) {
        return productoRepository.findById(idProd).orElseThrow();
    }

    public ResultadoResponse update(Producto producto) {
        try {
            var registro = productoRepository.save(producto);
            var mensaje = String.format("Producto con Id %s actualizado", registro.getIdProd());
            return new ResultadoResponse(true, mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    @Transactional
    public ResultadoResponse activar(String idProd) {
        var producto = productoRepository.findById(idProd).orElseThrow();
        producto.setEstado(true);
        var mensaje = String.format("Producto con Id %s activado", producto.getIdProd());
        return new ResultadoResponse(true, mensaje);
    }

    @Transactional
    public ResultadoResponse desactivar(String idProd) {
        var producto = productoRepository.findById(idProd).orElseThrow();
        producto.setEstado(false);
        var mensaje = String.format("Producto con Id %s desactivado", producto.getIdProd());
        return new ResultadoResponse(true, mensaje);
    }
}
