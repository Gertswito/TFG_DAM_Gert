package com.gert.tfgdam.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.gert.tfgdam.entity.FinalizarCompra;

@Component
public class VentaTemporalEnMemoria {
    private final Map<String, FinalizarCompra> temporalMap = new ConcurrentHashMap<>();

    public void guardar(String orderId, FinalizarCompra compra) {
        temporalMap.put(orderId, compra);
    }

    public FinalizarCompra obtener(String orderId) {
        return temporalMap.get(orderId);
    }

    public void eliminar(String orderId) {
        temporalMap.remove(orderId);
    }
}
