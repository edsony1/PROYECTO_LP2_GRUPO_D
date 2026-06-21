package com.cibertec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_producto_talla")
@Getter @Setter
public class ProductoTalla {
    @EmbeddedId
    private ProductoTallaId id;

    @ManyToOne
    @MapsId("idProd")
    @JoinColumn(name = "id_prod")
    private Producto producto;

    @ManyToOne
    @MapsId("idTalla")
    @JoinColumn(name = "idtalla")
    private Talla talla;
}
