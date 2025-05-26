package com.perfulandia.usuarios_service.service;

import java.util.List;
import java.util.Optional;

import com.perfulandia.usuarios_service.dto.ActualizarDireccionDTO;
import com.perfulandia.usuarios_service.dto.CrearDireccionDTO;
import com.perfulandia.usuarios_service.dto.DireccionResponseDTO;

public interface DireccionEnvioService {
  DireccionResponseDTO agregarDireccionAUsuario(String usuarioRut, CrearDireccionDTO crearDireccionDTO);

    List<DireccionResponseDTO> obtenerDireccionesPorUsuarioRut(String usuarioRut);

    Optional<DireccionResponseDTO> obtenerDireccionPorId(Long direccionId);

    DireccionResponseDTO actualizarDireccion(Long direccionId, ActualizarDireccionDTO actualizarDireccionDTO);

    void eliminarDireccion(Long direccionId);
}
