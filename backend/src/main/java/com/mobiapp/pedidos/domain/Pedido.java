package com.mobiapp.pedidos.domain;

import java.util.UUID;

public class Pedido {
    private final String id;
    private String descripcion;
    private EstadoPedido estado;

    public Pedido(String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.descripcion = descripcion;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
