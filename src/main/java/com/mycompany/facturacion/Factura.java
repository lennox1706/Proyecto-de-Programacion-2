/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenno
 */
public class Factura {

    private int numero;
    private LocalDate fecha;
    private String cedulaCliente;
    private String nombreCliente;
    private double total;
    private List<DetalleFactura> detalles;

    public Factura(int numero, LocalDate fecha, String cedulaCliente, String nombreCliente, double total) {

        this.numero = numero;
        this.fecha = fecha;
        this.cedulaCliente = cedulaCliente;
        this.nombreCliente = nombreCliente;
        this.total = total;
        this.detalles = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public double getTotal() {
        return total;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }
    
    public void agregarProducto(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "Busque un producto primero.");
        }

        DetalleFactura detalle = new DetalleFactura(
                producto.getCodigo(),
                producto.getDescripcion(),
                producto.getPrecio(),
                cantidad
        );

        detalles.add(detalle);
        calcularTotal();
    }

    public void quitarProducto(int posicion) {
        if (posicion < 0 || posicion >= detalles.size()) {
            throw new IllegalArgumentException(
                    "Seleccione un producto de la factura.");
        }

        detalles.remove(posicion);
        calcularTotal();
    }

    private void calcularTotal() {
        total = 0;

        for (DetalleFactura detalle : detalles) {
            total = total + detalle.getImporte();
        }
    }
}
