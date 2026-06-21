package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.DetallePedido;
import com.cibertec.repository.DetallePedidoRepository;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {
    private final DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> getAll() {
        return detallePedidoRepository.findAll();
    }

    public ResultadoResponse create(DetallePedido detalle) {
        try {
            detallePedidoRepository.save(detalle);
            return new ResultadoResponse(true, "Detalle registrado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al registrar detalle");
        }
    }
}
