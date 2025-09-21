package com.mobiapp.pedidos.repository;

import com.mobiapp.pedidos.domain.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
  Pedido save(Pedido pedido);
  Optional<Pedido> findById(String id);
  List<Pedido> findAll();
}
