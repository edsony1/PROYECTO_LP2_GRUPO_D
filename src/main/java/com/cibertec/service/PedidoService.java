package com.cibertec.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.cibertec.dto.ItemCarrito;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.DetallePedido;
import com.cibertec.model.DetallePedidoId;
import com.cibertec.model.Pedido;

import com.cibertec.repository.PedidoRepository;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> getByUsuario(Integer idUsuario) {
        return pedidoRepository.findByUsuario_IdUsuarioOrderByIdPedidoDesc(idUsuario);
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
    

    @Transactional
    public ResultadoResponse confirmarCompra(Integer idUsuario, List<ItemCarrito> carrito) {

        if (carrito == null || carrito.isEmpty()) {
            return new ResultadoResponse(false, "Tu carrito está vacío");
        }

        try {
            var usuario = usuarioService.getOne(idUsuario);

            for (var item : carrito) {
                var producto = productoService.getOne(item.getIdProd());
                if (producto.getStock() == null || producto.getStock() < item.getCantidad()) {
                    return new ResultadoResponse(false,
                            "Stock insuficiente para " + producto.getNombre()
                                    + " (disponible: " + (producto.getStock() == null ? 0 : producto.getStock()) + ")");
                }
            }

            var pedido = new Pedido();
            pedido.setFecha(LocalDate.now());
            pedido.setUsuario(usuario);

            for (var item : carrito) {

                var producto = productoService.getOne(item.getIdProd());

                var detalle = new DetallePedido();
                var id = new DetallePedidoId();
                id.setIdProd(item.getIdProd());
                detalle.setId(id);
                detalle.setPedido(pedido);
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecio(item.getPrecio());

                pedido.getDetalles().add(detalle);
            }

            var registro = pedidoRepository.save(pedido);

            for (var item : carrito) {
                productoService.descontarStock(item.getIdProd(), item.getCantidad());
            }

            return new ResultadoResponse(true, "Pedido Nro. " + registro.getIdPedido() + " registrado.");

        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al confirmar el pedido: " + e.getMessage());
        }
    }
}
