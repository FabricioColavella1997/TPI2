package com.bbva.clase07.adapters.mongo;

import org.springframework.stereotype.Component;

@Component
public class ProductosMapper {

    public ProductoDocument toDocument(ProductoDocument producto) {
        return new ProductoDocument(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    public ProductoDocument toDomain(ProductoDocument document) {
        return new ProductoDocument(
                document.getId(),
                document.getNombre(),
                document.getPrecio(),
                document.getStock()
        );
    }
}
