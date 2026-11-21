/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marii
 */

public class ProductoRepository {

    public void guardar(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto "
                + "(codigo, descripcion, precio, existencia) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, producto.getCodigo());
            consulta.setString(2, producto.getDescripcion());
            consulta.setDouble(3, producto.getPrecio());
            consulta.setInt(4, producto.getExistencia());

            consulta.executeUpdate();
        }
    }

    public void editar(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET descripcion = ?, "
                + "precio = ?, existencia = ? WHERE codigo = ?";

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, producto.getDescripcion());
            consulta.setDouble(2, producto.getPrecio());
            consulta.setInt(3, producto.getExistencia());
            consulta.setString(4, producto.getCodigo());

            consulta.executeUpdate();
        }
    }

    public void eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM producto WHERE codigo = ?";

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, codigo);
            consulta.executeUpdate();
        }
    }

    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT codigo, descripcion, precio, existencia "
                + "FROM producto ORDER BY codigo";

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql); ResultSet datos = consulta.executeQuery()) {

            while (datos.next()) {
                Producto producto = new Producto(
                        datos.getString("codigo"),
                        datos.getString("descripcion"),
                        datos.getDouble("precio"),
                        datos.getInt("existencia")
                );

                productos.add(producto);
            }
        }

        return productos;
    }

    public Producto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT codigo, descripcion, precio, existencia "
                + "FROM producto WHERE codigo = ?";

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, codigo);

            try (ResultSet datos = consulta.executeQuery()) {
                if (datos.next()) {
                    return new Producto(
                            datos.getString("codigo"),
                            datos.getString("descripcion"),
                            datos.getDouble("precio"),
                            datos.getInt("existencia")
                    );
                }
            }
        }

        return null;
    }

    public List<Producto> buscar(String texto) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT codigo, descripcion, precio, existencia " + "FROM producto WHERE codigo LIKE ? OR descripcion LIKE ? " + "ORDER BY codigo";

        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {

            String patron = "%" + texto.trim() + "%";
            consulta.setString(1, patron);
            consulta.setString(2, patron);

            try (ResultSet datos = consulta.executeQuery()) {
                while (datos.next()) {
                    Producto producto = new Producto(
                            datos.getString("codigo"),
                            datos.getString("descripcion"),
                            datos.getDouble("precio"),
                            datos.getInt("existencia")
                    );

                    productos.add(producto);
                }
            }
        }

        return productos;
    }
}
