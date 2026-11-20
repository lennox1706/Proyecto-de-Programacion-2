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
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.importe = importe;
    }
    
    public DetalleFactura(String codigoProducto, String descripcion, double precioUnitario, int cantidad) {

        this(codigoProducto, descripcion, precioUnitario,
                cantidad, precioUnitario * cantidad);

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }

        if (!Double.isFinite(precioUnitario) || precioUnitario < 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser un número mayor o igual a cero.");
        }

        if (!Double.isFinite(importe)) {
            throw new IllegalArgumentException(
                    "El importe supera el valor permitido.");
        }
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
