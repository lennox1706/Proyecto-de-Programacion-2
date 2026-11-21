/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturacion;

/**
 *
 * @author lenno
 */
public class DetalleFactura {

    private String codigoProducto;
    private String descripcion;
    private double precioUnitario;
    private int cantidad;
    private double importe;

    public DetalleFactura(String codigoProducto, String descripcion,double precioUnitario, int cantidad, double importe) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del producto es obligatorio.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion del producto es obligatoria.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (!Double.isFinite(precioUnitario) || precioUnitario < 0) {
            throw new IllegalArgumentException("El precio debe ser un numero mayor o igual a cero.");
        }
        if (!Double.isFinite(importe) || importe < 0) {
            throw new IllegalArgumentException("El importe debe ser un numero valido.");
        }
        double importeEsperado = Math.round(precioUnitario * cantidad * 100.0) / 100.0;
        if (Math.abs(importe - importeEsperado) > 0.01) {
            throw new IllegalArgumentException("El importe no coincide con el precio y la cantidad.");
        }

        this.codigoProducto = codigoProducto.trim();
        this.descripcion = descripcion.trim();
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.importe = importeEsperado;
    }
    
    public DetalleFactura(String codigoProducto, String descripcion, double precioUnitario, int cantidad) {

        this(codigoProducto, descripcion, precioUnitario,
                cantidad, Math.round(precioUnitario * cantidad * 100.0) / 100.0);
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getImporte() {
        return importe;
    }
}
