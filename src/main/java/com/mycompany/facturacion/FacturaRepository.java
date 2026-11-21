/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenno
 */
public class FacturaRepository {

    private static final String SELECT = "SELECT f.numero_factura, f.fecha, f.cedula_cliente, c.nombre, f.total " + "FROM factura f JOIN cliente c ON c.cedula = f.cedula_cliente";

    public int guardar(Factura factura) throws SQLException {

        if (factura == null) {
            throw new IllegalArgumentException("La factura no puede ser nula");
        }
        if (factura.getDetalles() == null || factura.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La factura debe contener al menos un producto");
        }

        String sqlFactura = "INSERT INTO factura (fecha, cedula_cliente, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_factura " + "(numero_factura, codigo_producto, precio_unitario, cantidad, importe) " + "VALUES (?, ?, ?, ?, ?)";

        double totalCalculado = 0;
        for (DetalleFactura detalle : factura.getDetalles()) {
            if (detalle == null) {
                throw new IllegalArgumentException("La factura contiene un detalle invalido.");
            }
            totalCalculado += detalle.getImporte();
        }
        totalCalculado = Math.round(totalCalculado * 100.0) / 100.0;

        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                int numero;
                try (PreparedStatement ps = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, Date.valueOf(factura.getFecha()));
                    ps.setString(2, factura.getCedulaCliente());
                    ps.setDouble(3, totalCalculado);
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("No se pudo obtener el numero de factura generado");
                        }
                        numero = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                    for (DetalleFactura detalle : factura.getDetalles()) {
                        ps.setInt(1, numero);
                        ps.setString(2, detalle.getCodigoProducto());
                        ps.setDouble(3, detalle.getPrecioUnitario());
                        ps.setInt(4, detalle.getCantidad());
                        ps.setDouble(5, detalle.getImporte());
                        ps.executeUpdate();
                    }
                }

                con.commit();
                return numero;
            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }
                throw e;
            }
        }
    }

    public List<Factura> listar() throws SQLException {
        return consultar(SELECT + " ORDER BY f.numero_factura", null);
    }

    public List<Factura> buscar(String texto) throws SQLException {

        String sql = SELECT + " WHERE f.cedula_cliente LIKE ? OR c.nombre LIKE ?" + " ORDER BY f.numero_factura";

        return consultar(sql, texto);
    }

    public Factura BuscarPorNumero(int numero) throws SQLException {

        String sql = SELECT + " WHERE f.numero_factura = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crear(rs);
                }
            }
        }

        return null;
    }

    public List<DetalleFactura> listarDetalle(int numero) throws SQLException {

        String sql = "SELECT d.codigo_producto, p.descripcion, " + "d.precio_unitario, d.cantidad, d.importe " + "FROM detalle_factura d " + "JOIN producto p ON p.codigo = d.codigo_producto " + "WHERE d.numero_factura = ? ORDER BY d.id_detalle";

        List<DetalleFactura> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numero);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetalleFactura(
                            rs.getString("codigo_producto"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio_unitario"),
                            rs.getInt("cantidad"),
                            rs.getDouble("importe")
                    ));
                }
            }
        }

        return lista;
    }

    private List<Factura> consultar(String sql, String texto) throws SQLException {

        List<Factura> lista = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (texto != null) {
                ps.setString(1, "%" + texto + "%");
                ps.setString(2, "%" + texto + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(crear(rs));
                }
            }
        }

        return lista;
    }

    private Factura crear(ResultSet rs) throws SQLException {

        return new Factura(
                rs.getInt("numero_factura"),
                rs.getDate("fecha").toLocalDate(),
                rs.getString("cedula_cliente"),
                rs.getString("nombre"),
                rs.getDouble("total")
        );
    }
}