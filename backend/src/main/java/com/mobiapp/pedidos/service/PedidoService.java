package com.mobiapp.pedidos.service;

import com.mobiapp.pedidos.domain.EstadoPedido;
import com.mobiapp.pedidos.domain.Pedido;
import com.mobiapp.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {
    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public Pedido crearPedido(String descripcion) {
        Pedido pedido = new Pedido(descripcion);
        return repository.save(pedido);
    }

    public Pedido obtenerPedido(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado: " + id));
    }

    public Pedido actualizarEstado(String id, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPedido(id);
        pedido.setEstado(nuevoEstado);
        repository.save(pedido);
        return pedido;
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }
}
