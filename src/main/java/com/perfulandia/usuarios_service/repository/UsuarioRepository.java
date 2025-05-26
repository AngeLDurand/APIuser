package com.perfulandia.usuarios_service.repository;
import com.perfulandia.usuarios_service.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{

}
