package com.coudevi.repository;

import com.coudevi.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository proporciona métodos CRUD listos para usar
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);
}
