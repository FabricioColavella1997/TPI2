package com.java2.tpi2SQLite.repository;

import com.java2.tpi2SQLite.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Los métodos básicos como save, findById, findAll, delete están heredados de JpaRepository
    // Aquí puedes agregar métodos personalizados si los necesitas
}
