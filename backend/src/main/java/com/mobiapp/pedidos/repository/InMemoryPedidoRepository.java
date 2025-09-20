package com.mobiapp.pedidos.repository;

import com.mobiapp.pedidos.domain.Pedido;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPedidoRepository implements PedidoRepository {
    private final Map<String, Pedido> storage = new ConcurrentHashMap<>();

    @Override
    public Pedido save(Pedido pedido) {
        storage.put(pedido.getId(), pedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}
