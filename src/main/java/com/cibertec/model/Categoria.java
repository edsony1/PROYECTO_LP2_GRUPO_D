package com.cibertec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_categorias")
@Getter @Setter
public class Categoria {
    @Id
    @Column(name = "idcategoria")
    private Integer idCategoria;

    @Column(name = "descripcion")
    private String descripcion;
}
