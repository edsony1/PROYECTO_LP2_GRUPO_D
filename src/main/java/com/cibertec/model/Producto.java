package com.cibertec.model;

import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
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

    @ManyToOne
    @JoinColumn(name = "idproveedor")
    private Proveedor proveedor;

    public String getEstadoDescripcion() {
        return estado != null && estado ? "Activo" : "Inactivo";
    }
}
