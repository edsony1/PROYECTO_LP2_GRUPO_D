package com.cibertec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_detalle_pedido")
@Getter @Setter
public class DetallePedido {
    @EmbeddedId
    private DetallePedidoId id;

    @ManyToOne
    @MapsId("idPedido")
    @JoinColumn(name = "idpedido")
    private Pedido pedido;

    @ManyToOne
    @MapsId("idProd")
    @JoinColumn(name = "id_prod")
    private Producto producto;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio")
    private Double precio;
}
