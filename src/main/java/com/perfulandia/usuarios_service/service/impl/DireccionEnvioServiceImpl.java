package com.perfulandia.usuarios_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.perfulandia.usuarios_service.dto.ActualizarDireccionDTO;
import com.perfulandia.usuarios_service.dto.CrearDireccionDTO;
import com.perfulandia.usuarios_service.dto.DireccionResponseDTO;
import com.perfulandia.usuarios_service.model.DireccionEnvio;
import com.perfulandia.usuarios_service.model.Usuario;
import com.perfulandia.usuarios_service.repository.DireccionEnvioRepository;
import com.perfulandia.usuarios_service.repository.UsuarioRepository;
import com.perfulandia.usuarios_service.service.DireccionEnvioService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DireccionEnvioServiceImpl implements DireccionEnvioService {

    private final DireccionEnvioRepository direccionEnvioRepository;
    private final UsuarioRepository usuarioRepository; // Necesario para asociar direcciones a usuarios

    @Autowired
    public DireccionEnvioServiceImpl(DireccionEnvioRepository direccionEnvioRepository,
                                     UsuarioRepository usuarioRepository) {
        this.direccionEnvioRepository = direccionEnvioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public DireccionResponseDTO agregarDireccionAUsuario(String usuarioRut, CrearDireccionDTO crearDireccionDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioRut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con RUT: " + usuarioRut));
                // En una app real: UsuarioNotFoundException

        DireccionEnvio nuevaDireccion = DireccionEnvio.builder()
                .calle(crearDireccionDTO.calle())
                .numero(crearDireccionDTO.numero())
                .comuna(crearDireccionDTO.comuna())
                .ciudad(crearDireccionDTO.ciudad())
                .usuario(usuario) // Asociamos la dirección al usuario encontrado
                .build();

        DireccionEnvio direccionGuardada = direccionEnvioRepository.save(nuevaDireccion);
        // Opcionalmente, añadir la dirección a la colección del usuario si la gestión de la relación es bidireccional
        // y queremos que el objeto 'usuario' esté actualizado en esta sesión.
        // usuario.getDireccionesEnvio().add(direccionGuardada); // Esto puede ser útil si luego usas el objeto 'usuario'

        return mapToDireccionResponseDTO(direccionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionResponseDTO> obtenerDireccionesPorUsuarioRut(String usuarioRut) {
        // Opción 1: Verificar si el usuario existe primero
        if (!usuarioRepository.existsById(usuarioRut)) {
            // Puedes lanzar una excepción si el usuario no existe, o devolver lista vacía.
            // Lanzar excepción es a menudo más claro si la expectativa es que el usuario DEBE existir.
            throw new RuntimeException("Usuario no encontrado con RUT: " + usuarioRut);
            // O: return Collections.emptyList(); si prefieres no lanzar excepción.
        }
        // Si usáramos el método que eliminamos de DireccionEnvioRepository:
        // return direccionEnvioRepository.findByUsuarioRut(usuarioRut) ...
        // Como lo eliminamos, cargamos el usuario y obtenemos sus direcciones:
        Usuario usuario = usuarioRepository.findById(usuarioRut).orElse(null); // Ya verificamos arriba, pero por si acaso.
        if (usuario == null || usuario.getDireccionesEnvio() == null) {
            return Collections.emptyList();
        }

        return usuario.getDireccionesEnvio().stream()
                .map(this::mapToDireccionResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DireccionResponseDTO> obtenerDireccionPorId(Long direccionId) {
        return direccionEnvioRepository.findById(direccionId)
                .map(this::mapToDireccionResponseDTO);
    }

    @Override
    @Transactional
    public DireccionResponseDTO actualizarDireccion(Long direccionId, ActualizarDireccionDTO actualizarDireccionDTO) {
        DireccionEnvio direccionExistente = direccionEnvioRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + direccionId));
                // En una app real: DireccionNotFoundException

        direccionExistente.setCalle(actualizarDireccionDTO.calle());
        direccionExistente.setNumero(actualizarDireccionDTO.numero());
        direccionExistente.setComuna(actualizarDireccionDTO.comuna());
        direccionExistente.setCiudad(actualizarDireccionDTO.ciudad());
        // El 'usuario_id' (USUARIOS_rut) no se cambia aquí, ya que esta es una actualización de la dirección en sí.
        // Si se quisiera reasignar una dirección a otro usuario, sería una operación diferente.

        DireccionEnvio direccionActualizada = direccionEnvioRepository.save(direccionExistente);
        return mapToDireccionResponseDTO(direccionActualizada);
    }

    @Override
    @Transactional
    public void eliminarDireccion(Long direccionId) {
        if (!direccionEnvioRepository.existsById(direccionId)) {
            throw new RuntimeException("Dirección no encontrada con ID: " + direccionId + ". No se puede eliminar.");
            // En una app real: DireccionNotFoundException
        }
        // La eliminación en cascada desde Usuario a DireccionEnvio (si un Usuario es eliminado)
        // está manejada por CascadeType.ALL en la entidad Usuario y ON DELETE CASCADE en la BD.
        // Aquí eliminamos una dirección específica directamente.
        direccionEnvioRepository.deleteById(direccionId);
    }

    // --- Métodos Helper para Mapeo ---
    private DireccionResponseDTO mapToDireccionResponseDTO(DireccionEnvio direccion) {
        if (direccion == null) {
            return null;
        }
        return new DireccionResponseDTO(
                direccion.getId(),
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getComuna(),
                direccion.getCiudad()
        );
    }
}