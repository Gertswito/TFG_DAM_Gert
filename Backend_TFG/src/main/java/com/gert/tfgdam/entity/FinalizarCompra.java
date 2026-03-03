package com.gert.tfgdam.entity;

import java.util.List;

public class FinalizarCompra {
    private Venta venta;
    private List<LineaVenta> lineasVenta;

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public List<LineaVenta> getLineasVenta() {
        return lineasVenta;
    }

    public void setLineasVenta(List<LineaVenta> lineasVenta) {
        this.lineasVenta = lineasVenta;
    }
}