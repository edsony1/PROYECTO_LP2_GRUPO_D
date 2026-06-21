package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.cibertec.model.ProductoTalla;
import com.cibertec.repository.ProductoTallaRepository;

@Service
@RequiredArgsConstructor
public class ProductoTallaService {
    private final ProductoTallaRepository productoTallaRepository;

    public List<ProductoTalla> getAll() {
        return productoTallaRepository.findAll();
    }
}
