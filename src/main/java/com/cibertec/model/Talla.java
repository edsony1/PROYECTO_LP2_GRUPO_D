package com.cibertec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_tallas")
@Getter @Setter
public class Talla {
    @Id
    @Column(name = "idtalla")
    private Integer idTalla;

    @Column(name = "descripcion")
    private String descripcion;
}
