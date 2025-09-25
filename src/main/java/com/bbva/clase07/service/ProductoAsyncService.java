package com.bbva.clase07.service;

import com.bbva.clase07.adapters.mongo.ProductoDocument;
import com.bbva.clase07.adapters.mongo.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class ProductoAsyncService {

    private final ProductoRepository productoRepository;
    private final Executor executor;

    @Autowired
    public ProductoAsyncService(ProductoRepository productoRepository, Executor asyncExecutor) {
        this.productoRepository = productoRepository;
        this.executor = asyncExecutor;
    }

    // Asíncrono sin delay
    public CompletableFuture<List<ProductoDocument>> listarTodosAsync() {
        return CompletableFuture.supplyAsync(() -> productoRepository.findAll(), executor);
    }

    // Síncrono
    public List<ProductoDocument> listarTodosSync() {
        return productoRepository.findAll();
    }

    // Asíncrono con delay
    public CompletableFuture<List<ProductoDocument>> listarTodosAsync(long delayMs) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(delayMs);
            return productoRepository.findAll();
        }, executor);
    }

    // Síncrono con delay
    public List<ProductoDocument> listarTodosSync(long delayMs) {
        sleep(delayMs);
        return productoRepository.findAll();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
