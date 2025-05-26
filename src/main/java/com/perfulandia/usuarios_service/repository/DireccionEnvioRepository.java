package com.perfulandia.usuarios_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.usuarios_service.model.DireccionEnvio;

@Repository
public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long>{
  
}
