package com.cibertec.model;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class ProductoTallaId implements Serializable {
    private String idProd;
    private Integer idTalla;
}
