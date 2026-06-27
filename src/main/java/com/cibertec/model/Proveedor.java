package com.cibertec.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_proveedor")
@Getter
@Setter
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproveedor")
    private Integer idProveedor;
    
    @Column(name = "razon_social")
    private String razonSocial;
    
    @Column(name = "celular")
    private String celular;
    
    @Column(name = "email")
    private String email;

    @Column(name = "estado")
    private boolean estado;
}
