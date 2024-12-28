package com.coudevi.controller;

import com.coudevi.entities.Producto;
import com.coudevi.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductoControllerTest {
    private ProductoRepository productoRepository;
    private ProductoController productoController;

    @BeforeEach
    void setUp(){
        // Crear un mock del repositorip
        productoRepository = mock(ProductoRepository.class);
        // Inyectar el mock en el controlador
        productoController = new ProductoController(productoRepository);
    }
    @Test
    void testObtenerProductoPorId_ProductoExiste(){
        //Configuración del escenario
        Producto producto = new Producto();
        producto.setId(1l);
        producto.setNombre("Laptop");
        producto.setPrecio(1500.00);

        // Simular el comportamiento del repositorio
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Llamar al método del controlador
        ResponseEntity<Producto> respuesta = productoController.obtenerPorId(1L);

        //Verificar la respuesta
        assertEquals(HttpStatusCode.valueOf(200), respuesta.getStatusCode());
        assertEquals("Laptop", respuesta.getBody().getNombre());
        assertEquals(1500.00, respuesta.getBody().getPrecio());
    }

    @Test
    void testObtenerProductoPorId_ProductoNoExiste(){
        // Simular el comportamiento del repositorio
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());
        //Llamar al método del controlador y capturar la excepción
        try {
            productoController.obtenerPorId(999L);
        }catch (Exception ex){
            assertEquals("404 NOT_FOUND \"El producto con ID 999 no existe.\"", ex.getMessage());
        }
    }
}
