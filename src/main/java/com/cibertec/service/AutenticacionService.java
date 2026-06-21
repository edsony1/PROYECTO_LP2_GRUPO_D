package com.cibertec.service;

import org.springframework.stereotype.Service;

import com.cibertec.dto.AutenticacionFilter;
import com.cibertec.dto.RegistroFilter;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.Rol;
import com.cibertec.model.Usuario;
import com.cibertec.repository.RolRepository;
import com.cibertec.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public Usuario authenticate(AutenticacionFilter filter) {

        return usuarioRepository.findByCorreoAndPassword(
                filter.getCorreo(),
                filter.getPassword()
        );
    }

    public ResultadoResponse registrar(RegistroFilter filter) {

        if (usuarioRepository.existsByCorreo(filter.getCorreo())) {
            return new ResultadoResponse(false, "Ese correo ya está registrado");
        }

        Rol rolCliente = rolRepository.findById(Rol.ID_CLIENTE).orElseThrow();

        Usuario usuario = new Usuario();
        usuario.setNombres(filter.getNombres());
        usuario.setApellidos(filter.getApellidos());
        usuario.setCorreo(filter.getCorreo());
        usuario.setPassword(filter.getPassword());
        usuario.setFechaNacimiento(filter.getFechaNacimiento());
        usuario.setEstado(true);
        usuario.setRol(rolCliente);

        try {
            usuarioRepository.save(usuario);
            return new ResultadoResponse(true, "Cuenta creada correctamente");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al crear la cuenta");
        }
    }

}

