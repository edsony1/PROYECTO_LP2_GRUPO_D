package com.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
