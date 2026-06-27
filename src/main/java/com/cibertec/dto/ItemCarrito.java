package com.cibertec.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCarrito {

    private String idProd;
    private String nombre;
    private String imagen;
    private Double precio;
    private Integer cantidad;

    public Double getSubtotal() {
        return precio * cantidad;
    }
}
