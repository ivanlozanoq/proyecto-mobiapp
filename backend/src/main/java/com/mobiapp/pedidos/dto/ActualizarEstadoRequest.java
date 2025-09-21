package com.mobiapp.pedidos.dto;

import com.mobiapp.pedidos.domain.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public class ActualizarEstadoRequest {
    @NotNull(message = "estado es requerido")
    private EstadoPedido estado;

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
