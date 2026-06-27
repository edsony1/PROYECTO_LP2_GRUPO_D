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

        if (filter.getCorreo() == null || !filter.getCorreo().matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            return new ResultadoResponse(false, "El formato del correo no es válido");
        }

        if (filter.getPassword() == null || filter.getPassword().length() < 6) {
            return new ResultadoResponse(false, "La contraseña debe tener al menos 6 caracteres");
        }

        if (filter.getNombres() == null || filter.getNombres().isBlank()) {
            return new ResultadoResponse(false, "El nombre es obligatorio");
        }
        if (filter.getApellidos() == null || filter.getApellidos().isBlank()) {
            return new ResultadoResponse(false, "Los apellidos son obligatorios");
        }

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