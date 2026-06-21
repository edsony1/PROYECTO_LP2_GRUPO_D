package com.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.model.ProductoTalla;
import com.cibertec.model.ProductoTallaId;

public interface ProductoTallaRepository extends JpaRepository<ProductoTalla, ProductoTallaId> {
}
