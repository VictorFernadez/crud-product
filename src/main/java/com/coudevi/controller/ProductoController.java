package com.coudevi.controller;

import com.coudevi.entities.Producto;
import com.coudevi.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/productos") // Ruta base para el controlador
public class ProductoController {
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    // Obtener todos los productos
    @GetMapping
    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto con ID " + id + " no existe."));
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto){
        if (productoRepository.existsByNombre(producto.getNombre())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Producto nuevoProducto = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto){
        return productoRepository.findById(id)
                .map(productoExistente -> {
                    productoExistente.setNombre(producto.getNombre());
                    productoExistente.setPrecio(producto.getPrecio());
                    productoRepository.save(productoExistente);
                    return  ResponseEntity.ok(productoExistente);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    productoRepository.delete(producto);
                    ResponseEntity<Void> respuesta = ResponseEntity.noContent().build(); // Declaramos explícitamente el tipo
                    return respuesta;
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Forzamos consistencia en el tipo
    }

    @GetMapping("/buscar")
    public List<Producto> buscarProductos(
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Double precioMin,
        @RequestParam(required = false) Double precioMax
    ){
        // Filtrado básico en función de los parámetros proporcionados
        return productoRepository.findAll().stream()
                .filter(producto -> nombre == null || producto.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(producto -> precioMin == null || producto.getPrecio() >= precioMin)
                .filter(producto -> precioMax == null || producto.getPrecio() <= precioMax)
                .toList();
    }

    @GetMapping("/paginado")
    public Page<Producto> obtenerProductosPaginados(@RequestParam int page, @RequestParam int size, @RequestParam(defaultValue = "id") String sortBy){
        return productoRepository.findAll(PageRequest.of(page,size, Sort.by(sortBy)));
    }

}
