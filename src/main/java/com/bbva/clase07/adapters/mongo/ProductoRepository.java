package com.bbva.clase07.adapters.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductoRepository extends MongoRepository<ProductoDocument,String> {
}
