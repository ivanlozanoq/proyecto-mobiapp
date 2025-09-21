package com.mobiapp.pedidos.controller;

import com.mobiapp.pedidos.domain.EstadoPedido;
import com.mobiapp.pedidos.domain.Pedido;
import com.mobiapp.pedidos.dto.ActualizarEstadoRequest;
import com.mobiapp.pedidos.dto.CrearPedidoRequest;
import com.mobiapp.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody CrearPedidoRequest request) {
        Pedido creado = pedidoService.crearPedido(request.getDescripcion());
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtener(@PathVariable String id) {
        Pedido pedido = pedidoService.obtenerPedido(id);
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable String id,
                                                   @Valid @RequestBody ActualizarEstadoRequest request) {
        EstadoPedido nuevo = request.getEstado();
        Pedido actualizado = pedidoService.actualizarEstado(id, nuevo);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return "pong";
    }
}
