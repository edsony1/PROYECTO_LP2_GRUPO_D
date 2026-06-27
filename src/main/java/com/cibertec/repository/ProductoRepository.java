package com.cibertec.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cibertec.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    List<Producto> findAllByOrderByIdProdDesc();

    @Query("SELECT p.idProd FROM Producto p ORDER BY p.idProd DESC LIMIT 1")
    String findLastId();

    List<Producto> findAllByEstadoTrue();

    List<Producto> findByCategoriaIdCategoria(Integer idCategoria);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("""
           SELECT p
           FROM Producto p
           WHERE 
               (:idCategoria IS NULL OR p.categoria.idCategoria = :idCategoria)
               AND
               (:idProveedor IS NULL OR p.proveedor.idProveedor = :idProveedor)
           """)
    List<Producto> findAllByFilters(
        @Param("idCategoria") Integer idCategoria,
        @Param("idProveedor") Integer idProveedor
    );
}