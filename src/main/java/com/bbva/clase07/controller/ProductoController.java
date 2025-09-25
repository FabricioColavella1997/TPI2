package com.bbva.clase07.controller;

import com.bbva.clase07.adapters.mongo.ProductoDocument;
import com.bbva.clase07.adapters.mongo.ProductoRepository;
import com.bbva.clase07.service.ProductoAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private final ProductoRepository productoRepository;
    private final ProductoAsyncService productoAsyncService;

    public ProductoController(ProductoRepository productoRepository,ProductoAsyncService productoAsyncService) {
        this.productoRepository = productoRepository;
        this.productoAsyncService = productoAsyncService;
    }

    @GetMapping
    public List<ProductoDocument> listarTodos() {
        return productoRepository.findAll();
    }

    @PostMapping
    public ProductoDocument crearProducto(@RequestBody ProductoDocument producto) {
        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public Optional<ProductoDocument> buscarPorId(@PathVariable String id) {
        return productoRepository.findById(id);
    }

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<ProductoDocument>>> listarAsync(
            @RequestParam(defaultValue = "0") long delayMs) {
        return productoAsyncService.listarTodosAsync(delayMs).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/sync")
    public ResponseEntity<List<ProductoDocument>> listarSync(
            @RequestParam(defaultValue = "0") long delayMs) {
        return ResponseEntity.ok(productoAsyncService.listarTodosSync(delayMs));
    }

    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo(
            @RequestParam(defaultValue = "0") long syncDelay,
            @RequestParam(defaultValue = "0") long asyncDelay) {

        long t0 = System.currentTimeMillis();
        var cf = productoAsyncService.listarTodosAsync(asyncDelay);

        long syncStart = System.currentTimeMillis();
        var syncRes = productoAsyncService.listarTodosSync(syncDelay);
        long syncEnd = System.currentTimeMillis();

        var asyncRes = cf.join();
        long asyncEnd = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return ResponseEntity.ok(Map.of(
                "t0_timestamp", sdf.format(new Date(t0)),
                "t0_millis", t0,
                "sync", Map.of(
                        "start_timestamp", sdf.format(new Date(syncStart)),
                        "end_timestamp", sdf.format(new Date(syncEnd)),
                        "start_millis", syncStart,
                        "end_millis", syncEnd,
                        "elapsed_ms", (syncEnd - syncStart),
                        "result_size", syncRes.size()
                ),
                "async", Map.of(
                        "start_timestamp", sdf.format(new Date(t0)),
                        "end_timestamp", sdf.format(new Date(asyncEnd)),
                        "start_millis", t0,
                        "end_millis", asyncEnd,
                        "elapsed_ms_since_t0", (asyncEnd - t0),
                        "result_size", asyncRes.size()
                ),
                "note", "El async corre en paralelo (pool) mientras el sync bloquea el hilo del request."
        ));
    }
}