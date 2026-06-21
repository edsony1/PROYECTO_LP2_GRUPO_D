package com.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByCorreoAndPassword(String correo, String password);

    boolean existsByCorreo(String correo);

}
