package com.coudevi.controller;
import com.coudevi.entities.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.Validation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProductoValidationTest {
    private Validator validador;

    @BeforeEach
    void configurarValidador() {
        // Crear el validador usando javax.validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validador = factory.getValidator();
    }
    @Test
    void testNombreNoDebeSerVacio() {
        Producto producto = new Producto();
        producto.setNombre(""); // Nombre inválido
        producto.setPrecio(100.00); // Precio válido

        Set<ConstraintViolation<Producto>> errores = validador.validate(producto);

        // Verificar que hay errores y que el mensaje es el esperado
        assertFalse(errores.isEmpty());
        assertEquals("El nombre no puede estar vacío.", errores.iterator().next().getMessage());
    }
    @Test
    void testPrecioDebeSerPositivo() {
        Producto producto = new Producto();
        producto.setNombre("Teclado"); // Nombre válido
        producto.setPrecio(-10.00); // Precio inválido

        Set<ConstraintViolation<Producto>> errores = validador.validate(producto);

        // Verificar que hay errores y que el mensaje es el esperado
        assertFalse(errores.isEmpty());
        assertEquals("El precio debe ser mayor que 0", errores.iterator().next().getMessage());
    }
    @Test
    void testProductoValido() {
        Producto producto = new Producto();
        producto.setNombre("Monitor");
        producto.setPrecio(300.00);

        Set<ConstraintViolation<Producto>> errores = validador.validate(producto);

        // Verificar que no hay errores
        assertEquals(0, errores.size());
    }
}
