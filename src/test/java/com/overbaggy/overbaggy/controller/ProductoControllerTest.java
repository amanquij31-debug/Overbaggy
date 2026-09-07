package com.overbaggy.overbaggy.controller;

import tools.jackson.databind.ObjectMapper;
import com.overbaggy.overbaggy.entity.Producto;
import com.overbaggy.overbaggy.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de la capa web (endpoints REST) usando MockMvc.
 * Verifican los métodos HTTP (GET, POST, PUT, DELETE) y los códigos
 * de estado devueltos por cada endpoint de ProductoController.
 * Equivalen, a nivel de código, a las pruebas manuales realizadas
 * con Postman durante el desarrollo.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto("Mouse inalámbrico", "Mouse óptico 1600 DPI", 79.90, 35, "Periféricos");
        producto.setId(1L);
    }

    @Test
    void listarProductos_debeRetornar200YLaLista() throws Exception {
        when(productoService.listarProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mouse inalámbrico"));
    }

    @Test
    void obtenerProducto_cuandoExiste_debeRetornar200() throws Exception {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerProducto_cuandoNoExiste_debeRetornar404() throws Exception {
        when(productoService.obtenerProductoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearProducto_debeRetornar200YElProductoCreado() throws Exception {
        when(productoService.guardarProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mouse inalámbrico"));
    }

    @Test
    void actualizarProducto_cuandoExiste_debeRetornar200() throws Exception {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));
        when(productoService.guardarProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarProducto_cuandoNoExiste_debeRetornar404() throws Exception {
        when(productoService.obtenerProductoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProducto_cuandoExiste_debeRetornar204() throws Exception {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));
        doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarProducto_cuandoNoExiste_debeRetornar404() throws Exception {
        when(productoService.obtenerProductoPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());
    }
}
