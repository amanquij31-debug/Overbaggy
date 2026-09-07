package com.overbaggy.overbaggy.service;

import com.overbaggy.overbaggy.entity.Producto;
import com.overbaggy.overbaggy.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de ProductoService siguiendo el enfoque TDD:
 * cada método fue validado primero contra el comportamiento esperado
 * del repositorio (mockeado con Mockito) antes de confirmar la
 * implementación final del servicio.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto("Teclado mecánico", "Teclado RGB switch rojo", 149.90, 20, "Periféricos");
        producto.setId(1L);
    }

    @Test
    void listarProductos_debeRetornarTodosLosProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarProductos();

        assertEquals(1, resultado.size());
        assertEquals("Teclado mecánico", resultado.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void obtenerProductoPorId_cuandoExiste_debeRetornarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.obtenerProductoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void obtenerProductoPorId_cuandoNoExiste_debeRetornarVacio() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.obtenerProductoPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void guardarProducto_debePersistirYRetornarElProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto guardado = productoService.guardarProducto(producto);

        assertEquals("Teclado mecánico", guardado.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void eliminarProducto_debeInvocarAlRepositorio() {
        doNothing().when(productoRepository).deleteById(anyLong());

        productoService.eliminarProducto(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
