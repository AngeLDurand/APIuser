package com.perfulandia.usuarios_service.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.perfulandia.usuarios_service.dto.ActualizarUsuarioDTO;
import com.perfulandia.usuarios_service.dto.CrearUsuarioDTO;
import com.perfulandia.usuarios_service.dto.UsuarioResponseDTO;
import com.perfulandia.usuarios_service.model.Usuario;
import com.perfulandia.usuarios_service.repository.UsuarioRepository;
import com.perfulandia.usuarios_service.service.UsuarioService;
import org.springframework.transaction.annotation.Transactional;


@Service // Marca esta clase como un bean de servicio de Spring
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Inyectar para hashear contraseñas

    @Autowired // Inyección de dependencias vía constructor (buena práctica)
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // Asegura que la operación sea atómica
    public UsuarioResponseDTO crearUsuario(CrearUsuarioDTO crearUsuarioDTO) {
        // Verificar si el RUT ya existe
        if (usuarioRepository.existsById(crearUsuarioDTO.rut())) {
            throw new RuntimeException("El RUT '" + crearUsuarioDTO.rut() + "' ya está registrado.");
            // En una aplicación real, usarías excepciones personalizadas, ej: RutYaExistenteException
        }

        Usuario nuevoUsuario = Usuario.builder()
                .rut(crearUsuarioDTO.rut())
                .nombre(crearUsuarioDTO.nombre())
                .apellido(crearUsuarioDTO.apellido())
                .email(crearUsuarioDTO.email())
                .passwordHash(passwordEncoder.encode(crearUsuarioDTO.password())) // Hashear la contraseña
                // .activo(true) // Si tuvieras este campo en la entidad, inicializarlo
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return convertToUsuarioResponseDTO(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true) // Operación de solo lectura, optimiza la transacción
    public Optional<UsuarioResponseDTO> obtenerUsuarioPorRut(String rut) {
        return usuarioRepository.findById(rut)
                .map(this::convertToUsuarioResponseDTO); // Mapea Usuario a UsuarioResponseDTO si está presente
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertToUsuarioResponseDTO) // Mapea cada Usuario a UsuarioResponseDTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(String rut, ActualizarUsuarioDTO actualizarUsuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(rut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con RUT: " + rut));
                // En una aplicación real: UsuarioNotFoundException

        // Actualizar los campos permitidos
        usuarioExistente.setNombre(actualizarUsuarioDTO.nombre());
        usuarioExistente.setApellido(actualizarUsuarioDTO.apellido());
        // No se actualiza el email ni la contraseña aquí, según el DTO

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return convertToUsuarioResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(String rut) {
        if (!usuarioRepository.existsById(rut)) {
            throw new RuntimeException("Usuario no encontrado con RUT: " + rut + ". No se puede eliminar.");
            // En una aplicación real: UsuarioNotFoundException
        }
        usuarioRepository.deleteById(rut);
    }

    // --- Métodos Helper para Mapeo ---
    private UsuarioResponseDTO convertToUsuarioResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getRut(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail()
        );
    }
}