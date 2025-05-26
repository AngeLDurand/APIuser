package com.perfulandia.usuarios_service.dto;

public record DireccionResponseDTO(
    Long id,
    String calle,
    String numero,
    String comuna,
    String ciudad) {

}
