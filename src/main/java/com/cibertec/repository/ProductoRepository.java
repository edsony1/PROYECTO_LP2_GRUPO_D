package com.cibertec.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findAllByOrderByPrecioDesc();
    List<Producto> findByNombreContaining(String nombre);
    List<Producto> findByCategoria_IdCategoria(Integer idCategoria);
}
