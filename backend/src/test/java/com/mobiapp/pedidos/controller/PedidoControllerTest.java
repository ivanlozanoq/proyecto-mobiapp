package com.mobiapp.pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiapp.pedidos.domain.EstadoPedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearPedido_debeRetornar200YPedidoConId() throws Exception {
        Map<String, String> body = Map.of("descripcion", "Pedido de prueba");

        MvcResult result = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.descripcion").value("Pedido de prueba"))
                .andExpect(jsonPath("$.estado").value(EstadoPedido.PENDIENTE.name()))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Map<?, ?> response = objectMapper.readValue(json, Map.class);
        assertThat(response.get("id")).isNotNull();
    }

    @Test
    void obtenerPedido_porId_debeRetornar200() throws Exception {
        // Primero crear
        Map<String, String> body = Map.of("descripcion", "Para consultar");
        MvcResult resultCrear = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();
        Map<?, ?> creado = objectMapper.readValue(resultCrear.getResponse().getContentAsString(), Map.class);
        String id = (String) creado.get("id");

        // Luego consultar
        mockMvc.perform(get("/api/pedidos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.descripcion").value("Para consultar"));
    }

    @Test
    void actualizarEstado_debeRetornar200YEstadoActualizado() throws Exception {
        // Crear
        Map<String, String> body = Map.of("descripcion", "Para actualizar");
        MvcResult resultCrear = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();
        Map<?, ?> creado = objectMapper.readValue(resultCrear.getResponse().getContentAsString(), Map.class);
        String id = (String) creado.get("id");

        // Actualizar estado
        Map<String, String> patch = Map.of("estado", EstadoPedido.FINALIZADO.name());
        mockMvc.perform(patch("/api/pedidos/" + id + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(EstadoPedido.FINALIZADO.name()));
    }

    @Test
    void obtenerPedido_inexistente_debeRetornar404() throws Exception {
        String randomId = UUID.randomUUID().toString();
        mockMvc.perform(get("/api/pedidos/" + randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void crearPedido_conDescripcionVacia_debeRetornar400() throws Exception {
        Map<String, String> body = Map.of("descripcion", " ");
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validacion"))
                .andExpect(jsonPath("$.details.descripcion").exists());
    }

    @Test
    void actualizarEstado_sinEstado_debeRetornar400() throws Exception {
        // Crear un pedido válido primero
        Map<String, String> body = Map.of("descripcion", "Para validar estado");
        MvcResult resultCrear = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();
        Map<?, ?> creado = objectMapper.readValue(resultCrear.getResponse().getContentAsString(), Map.class);
        String id = (String) creado.get("id");

        // Enviar PATCH sin campo estado
        mockMvc.perform(patch("/api/pedidos/" + id + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validacion"))
                .andExpect(jsonPath("$.details.estado").exists());
    }
}
