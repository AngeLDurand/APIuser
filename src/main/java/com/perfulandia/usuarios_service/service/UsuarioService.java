package com.perfulandia.usuarios_service.service;

import java.util.List;
import java.util.Optional;

import com.perfulandia.usuarios_service.dto.ActualizarUsuarioDTO;
import com.perfulandia.usuarios_service.dto.CrearUsuarioDTO;
import com.perfulandia.usuarios_service.dto.UsuarioResponseDTO;

public interface UsuarioService {
    UsuarioResponseDTO crearUsuario(CrearUsuarioDTO crearUsuarioDTO);

    Optional<UsuarioResponseDTO> obtenerUsuarioPorRut(String rut);

    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();

    UsuarioResponseDTO actualizarUsuario(String rut, ActualizarUsuarioDTO actualizarUsuarioDTO);

    void eliminarUsuario(String rut);
}
