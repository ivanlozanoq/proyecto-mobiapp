package com.mobiapp.pedidos.dto;

import jakarta.validation.constraints.NotBlank;

public class CrearPedidoRequest {
    @NotBlank(message = "descripcion no debe estar vacía")
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
