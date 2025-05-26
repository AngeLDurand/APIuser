package com.perfulandia.usuarios_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; // Para construir la URI de Location

import com.perfulandia.usuarios_service.dto.ActualizarUsuarioDTO;
import com.perfulandia.usuarios_service.dto.CrearUsuarioDTO;
import com.perfulandia.usuarios_service.dto.UsuarioResponseDTO;
import com.perfulandia.usuarios_service.service.UsuarioService;

import jakarta.validation.Valid; // Para validaciones (necesitarías spring-boot-starter-validation)
import java.net.URI;
import java.util.List;

@RestController // Combina @Controller y @ResponseBody, los métodos devuelven datos directamente en el cuerpo de la respuesta.
@RequestMapping("/api/v1/usuarios") // Ruta base para todos los endpoints de este controlador
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired // Inyección de dependencias vía constructor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para CREAR un nuevo usuario
    // POST /api/v1/usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO) {
        UsuarioResponseDTO usuarioCreado = usuarioService.crearUsuario(crearUsuarioDTO);
        // Construir la URI del nuevo recurso creado para la cabecera Location
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Toma la URI actual (/api/v1/usuarios)
                .path("/{rut}")       // Le añade el path variable del nuevo recurso
                .buildAndExpand(usuarioCreado.rut()) // Reemplaza {rut} con el RUT del usuario creado
                .toUri();
        return ResponseEntity.created(location).body(usuarioCreado); // Devuelve 201 Created
    }

    // Endpoint para OBTENER un usuario por su RUT
    // GET /api/v1/usuarios/{rut}
    @GetMapping("/{rut}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorRut(@PathVariable String rut) {
        return usuarioService.obtenerUsuarioPorRut(rut)
                .map(usuarioDTO -> ResponseEntity.ok(usuarioDTO)) // Si se encuentra, devuelve 200 OK con el usuario
                .orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }

    // Endpoint para OBTENER todos los usuarios
    // GET /api/v1/usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content si la lista está vacía
        }
        return ResponseEntity.ok(usuarios); // Devuelve 200 OK con la lista de usuarios
    }

    // Endpoint para ACTUALIZAR un usuario existente
    // PUT /api/v1/usuarios/{rut}
    @PutMapping("/{rut}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable String rut,
            @Valid @RequestBody ActualizarUsuarioDTO actualizarUsuarioDTO) {
        // La lógica para manejar "usuario no encontrado" debería estar en el servicio,
        // que podría lanzar una excepción. Aquí asumimos que si el servicio
        // no lanza excepción, la actualización fue (o será) exitosa.
        // Si el servicio devuelve el objeto actualizado, podemos devolverlo.
        try {
            UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuario(rut, actualizarUsuarioDTO);
            return ResponseEntity.ok(usuarioActualizado); // Devuelve 200 OK
        } catch (RuntimeException e) { // Asumiendo que el servicio lanza RuntimeException si no se encuentra
            // En una app real, capturarías excepciones más específicas (ej. UsuarioNotFoundException)
            // y tendrías un @ControllerAdvice para manejar excepciones globalmente.
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }

    // Endpoint para ELIMINAR un usuario
    // DELETE /api/v1/usuarios/{rut}
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String rut) {
        try {
            usuarioService.eliminarUsuario(rut);
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content (éxito, sin cuerpo de respuesta)
        } catch (RuntimeException e) { // Asumiendo que el servicio lanza RuntimeException si no se encuentra
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found
        }
    }
}