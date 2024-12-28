package com.coudevi.controller;

import com.coudevi.entities.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductoIntegrationTest {
    @LocalServerPort
    private int puerto;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCrearProducto_Exito(){
        // Producto de prueba
        Producto producto = new Producto();
        producto.setNombre("Audífonos Negros");
        producto.setPrecio(100.00);

        // Solictud POST
        ResponseEntity<Producto> response = restTemplate.postForEntity(
          "http://localhost:" + puerto + "/api/productos", producto, Producto.class);

        // Verificar la respuesta
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Audífonos Negros", response.getBody().getNombre());
    }
}
