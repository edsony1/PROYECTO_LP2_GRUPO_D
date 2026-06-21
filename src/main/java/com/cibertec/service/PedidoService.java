package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.Pedido;
import com.cibertec.repository.PedidoRepository;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    public Pedido getOne(Integer idPedido) {
        return pedidoRepository.findById(idPedido).orElseThrow();
    }

    public ResultadoResponse create(Pedido pedido) {
        try {
            var registro = pedidoRepository.save(pedido);
            return new ResultadoResponse(true, "Pedido Nro. " + registro.getIdPedido() + " registrado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al registrar pedido");
        }
    }

    public ResultadoResponse update(Pedido pedido) {
        try {
            var registro = pedidoRepository.save(pedido);
            return new ResultadoResponse(true, "Pedido Nro. " + registro.getIdPedido() + " actualizado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al actualizar pedido");
        }
    }
}
