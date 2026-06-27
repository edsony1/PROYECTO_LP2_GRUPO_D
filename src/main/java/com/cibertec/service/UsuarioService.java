package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.Usuario;
import com.cibertec.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> getAllClientes() {
        return usuarioRepository.findByRolIdRolNot(1);
    }

    public Usuario getOne(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow();
    }

    public ResultadoResponse create(Usuario usuario) {
        try {
            var registro = usuarioRepository.save(usuario);
            return new ResultadoResponse(true, "Usuario " + registro.getCorreo() + " registrado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al registrar usuario");
        }
    }

    public ResultadoResponse update(Usuario usuario) {
        try {
            var registro = usuarioRepository.save(usuario);
            return new ResultadoResponse(true, "Usuario " + registro.getCorreo() + " actualizado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al actualizar usuario");
        }
    }

    @Transactional
    public ResultadoResponse updatePerfil(Integer idUsuario, String nombres, String apellidos, String correo,
                                          String passwordActual, String passwordNueva, String passwordConfirmar) {

        if (usuarioRepository.existsByCorreoAndIdUsuarioNot(correo, idUsuario)) {
            return new ResultadoResponse(false, "Ese correo ya está en uso por otra cuenta");
        }

        // Validar cambio de contraseña solo si el usuario llenó algo
        boolean cambiarPassword = passwordNueva != null && !passwordNueva.isBlank();
        if (cambiarPassword) {
            if (passwordActual == null || passwordActual.isBlank()) {
                return new ResultadoResponse(false, "Debes ingresar tu contraseña actual");
            }
            if (passwordNueva.length() < 6) {
                return new ResultadoResponse(false, "La nueva contraseña debe tener al menos 6 caracteres");
            }
            if (!passwordNueva.equals(passwordConfirmar)) {
                return new ResultadoResponse(false, "Las contraseñas nuevas no coinciden");
            }
        }

        try {
            var usuario = usuarioRepository.findById(idUsuario).orElseThrow();

            if (cambiarPassword) {
                if (!passwordActual.equals(usuario.getPassword())) {
                    return new ResultadoResponse(false, "La contraseña actual es incorrecta");
                }
                usuario.setPassword(passwordNueva);
            }

            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCorreo(correo);
            return new ResultadoResponse(true, "Perfil actualizado correctamente");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al actualizar el perfil");
        }
    }

    @Transactional
    public ResultadoResponse activar(Integer idUsuario) {
        var usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        usuario.setEstado(true);
        return new ResultadoResponse(true, "Usuario " + usuario.getCorreo() + " activado.");
    }

    @Transactional
    public ResultadoResponse desactivar(Integer idUsuario) {
        var usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        usuario.setEstado(false);
        return new ResultadoResponse(true, "Usuario " + usuario.getCorreo() + " desactivado.");
    }

    public ResultadoResponse delete(Integer idUsuario) {
        try {
            usuarioRepository.deleteById(idUsuario);
            return new ResultadoResponse(true, "Usuario eliminado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al eliminar usuario");
        }
    }
}