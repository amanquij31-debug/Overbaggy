package com.overbaggy.overbaggy.repository;

import com.overbaggy.overbaggy.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}