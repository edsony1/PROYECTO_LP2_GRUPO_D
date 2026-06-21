package com.cibertec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.cibertec.dto.ResultadoResponse;
import com.cibertec.model.Usuario;
import com.cibertec.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
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

    public ResultadoResponse delete(Integer idUsuario) {
        try {
            usuarioRepository.deleteById(idUsuario);
            return new ResultadoResponse(true, "Usuario eliminado.");
        } catch (Exception e) {
            return new ResultadoResponse(false, "Error al eliminar usuario");
        }
    }
}
