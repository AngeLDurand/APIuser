package com.perfulandia.usuarios_service.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {
  @Id // Marca este campo como la Clave Primaria
    @Column(name = "rut", length = 10, nullable = false, unique = true)
    private String rut;

    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    // En tu diagrama, "apellido" no tenía un asterisco, así que asumiré que es nullable.
    // Si debe ser no nulo, cambia nullable = false
    @Column(name = "apellido", length = 255, nullable = false)
    private String apellido;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    // El nombre de la columna en la BD es "password" según tu diagrama.
    // En Java, es buena práctica llamar al campo de manera que indique que es un hash.
    @Column(name = "password", length = 255, nullable = false)
    private String passwordHash;

    // Relación Uno-a-Muchos con DireccionesEnvio
    // "mappedBy = "usuario"" indica que el lado dueño de la relación está en la entidad DireccionEnvio,
    // en un campo llamado "usuario".
    // CascadeType.ALL: Operaciones (persist, merge, remove, etc.) en Usuario se propagan a sus direcciones.
    // orphanRemoval = true: Si una dirección se quita de esta lista, se elimina de la BD.
    // fetch = FetchType.LAZY: Las direcciones no se cargan de la BD hasta que se acceden explícitamente.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // Asegura que la lista se inicialice si usas el Builder
    private List<DireccionEnvio> direccionesEnvio = new ArrayList<>();
}
