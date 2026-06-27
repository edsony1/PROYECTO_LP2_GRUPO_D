package com.cibertec.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    // Listar proveedores ordenados por ID descendente
    List<Proveedor> findAllByOrderByIdProveedorDesc();

    // Buscar proveedores por razón social
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
    
    List<Proveedor> findAllByEstadoTrue();
    
}
