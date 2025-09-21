package com.mobiapp.pedidos.repository.jpa;

import com.mobiapp.pedidos.domain.Pedido;
import com.mobiapp.pedidos.repository.PedidoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("!inmemory")
public class JpaPedidoRepositoryAdapter implements PedidoRepository {

    private final SpringDataPedidoJpaRepository jpaRepository;

    public JpaPedidoRepositoryAdapter(SpringDataPedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pedido save(Pedido pedido) {
        return jpaRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Pedido> findAll() {
        return jpaRepository.findAll();
    }
}
