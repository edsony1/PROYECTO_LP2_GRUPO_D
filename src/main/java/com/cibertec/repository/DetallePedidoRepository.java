package com.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.model.DetallePedido;
import com.cibertec.model.DetallePedidoId;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId> {
}
