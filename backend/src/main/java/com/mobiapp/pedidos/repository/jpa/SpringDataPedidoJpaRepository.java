package com.mobiapp.pedidos.repository.jpa;

import com.mobiapp.pedidos.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPedidoJpaRepository extends JpaRepository<Pedido, String> {
}
