package com.perfulandia.usuarios_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "direcciones_envio")
public class DireccionEnvio {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que MySQL maneje el auto-incremento del ID
    @Column(name = "id", nullable = false, unique = true) // El 'id' de la tabla direcciones_envio
    private Long id; // NUMBER(9) en tu diagrama se mapea bien a Long en Java

    @Column(name = "calle", length = 255, nullable = false)
    private String calle;

    @Column(name = "numero", length = 255, nullable = false)
    private String numero; // Mantenemos 255 según tu diagrama simplificado

    @Column(name = "comuna", length = 255, nullable = false)
    private String comuna;

    @Column(name = "ciudad", length = 255, nullable = false)
    private String ciudad;

    // Relación Muchos-a-Uno con Usuario
    // La columna 'USUARIOS_rut' en esta tabla 'direcciones_envio'
    // hace referencia a la columna 'rut' en la tabla 'usuarios'.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY es generalmente preferido para el rendimiento
    @JoinColumn(name = "USUARIOS_rut", referencedColumnName = "rut", nullable = false)
    private Usuario usuario;
}
