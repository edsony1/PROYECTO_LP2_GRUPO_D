package com.cibertec.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    List<Proveedor> findAllByOrderByIdProveedorDesc();

    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
    
    List<Proveedor> findAllByEstadoTrue();
    
}
