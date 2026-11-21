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

public class ClienteRepository {

    private static final String COLUMNAS = "cedula, nombre, telefono, correo";

    public void guardar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (cedula, nombre, telefono, correo) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cliente.getCedula());
            consulta.setString(2, cliente.getNombre());
            consulta.setString(3, cliente.getTelefono());
            consulta.setString(4, cliente.getCorreo());
            consulta.executeUpdate();
        }
    }

    public void editar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre = ?, telefono = ?, correo = ? WHERE cedula = ?";
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cliente.getNombre());
            consulta.setString(2, cliente.getTelefono());
            consulta.setString(3, cliente.getCorreo());
            consulta.setString(4, cliente.getCedula());
            consulta.executeUpdate();
        }
    }

    public void eliminar(String cedula) throws SQLException {
        String sql = "DELETE FROM cliente WHERE cedula = ?";
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cedula);
            consulta.executeUpdate();
        }
    }

    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM cliente ORDER BY nombre";
        List<Cliente> lista = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql); ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                lista.add(crearCliente(resultado));
            }
        }
        return lista;
    }

    public List<Cliente> buscar(String texto) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM cliente "
                + "WHERE cedula LIKE ? OR nombre LIKE ? ORDER BY nombre";
        List<Cliente> lista = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {
            String patron = "%" + texto.trim() + "%";
            consulta.setString(1, patron);
            consulta.setString(2, patron);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    lista.add(crearCliente(resultado));
                }
            }
        }
        return lista;
    }

    public Cliente buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM cliente WHERE cedula = ?";
        try (Connection conexion = ConexionBD.obtenerConexion(); PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cedula.trim());
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? crearCliente(resultado) : null;
            }
        }
    }


    private Cliente crearCliente(ResultSet resultado) throws SQLException {
        return new Cliente(
                resultado.getString("cedula"),
                resultado.getString("nombre"),
                resultado.getString("telefono"),
                resultado.getString("correo"));
    }
}
