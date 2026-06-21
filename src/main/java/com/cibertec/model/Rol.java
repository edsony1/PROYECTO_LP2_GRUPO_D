package com.cibertec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_roles")
@Getter @Setter
public class Rol {

    public static final Integer ID_ADMIN = 1;
    public static final Integer ID_CLIENTE = 2;

    @Id
    @Column(name = "idrol")
    private Integer idRol;

    @Column(name = "descripcion")
    private String descripcion;

    public boolean isAdmin() {
        return ID_ADMIN.equals(idRol);
    }
}
