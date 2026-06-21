package com.cibertec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_productos")
@Getter @Setter
public class Producto {
    @Id
    @Column(name = "id_prod")
    private String idProd;

    @Column(name = "nom_prod")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "idcategoria")
    private Categoria categoria;
}
