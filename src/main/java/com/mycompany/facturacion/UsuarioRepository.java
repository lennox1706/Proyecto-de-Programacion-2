package com.mycompany.facturacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla usuario (MySQL).
 * Todas las consultas usan PreparedStatement.
 */
public class UsuarioRepository {

    public void guardar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (cedula, nombre, contrasena) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, usuario.getCedula());
            consulta.setString(2, usuario.getNombre());
            consulta.setString(3, usuario.getContrasena());
            consulta.executeUpdate();
        }
    }

    public void editar(Usuario usuario) throws SQLException {
        // La cedula es la llave primaria: no se modifica, solo se usa en el WHERE.
        String sql = "UPDATE usuario SET nombre = ?, contrasena = ? WHERE cedula = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, usuario.getNombre());
            consulta.setString(2, usuario.getContrasena());
            consulta.setString(3, usuario.getCedula());
            consulta.executeUpdate();
        }
    }

    public void eliminar(String cedula) throws SQLException {
        String sql = "DELETE FROM usuario WHERE cedula = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cedula);
            consulta.executeUpdate();
        }
    }

    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT cedula, nombre, contrasena FROM usuario ORDER BY nombre";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet datos = consulta.executeQuery()) {
            while (datos.next()) {
                usuarios.add(crearUsuario(datos));
            }
        }
        return usuarios;
    }

    /** Busca por cedula o por nombre (busqueda parcial). */
    public List<Usuario> buscar(String texto) throws SQLException {
        String sql = "SELECT cedula, nombre, contrasena FROM usuario "
                   + "WHERE cedula LIKE ? OR nombre LIKE ? ORDER BY nombre";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, "%" + texto + "%");
            consulta.setString(2, "%" + texto + "%");
            try (ResultSet datos = consulta.executeQuery()) {
                while (datos.next()) {
                    usuarios.add(crearUsuario(datos));
                }
            }
        }
        return usuarios;
    }

    public Usuario buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT cedula, nombre, contrasena FROM usuario WHERE cedula = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cedula);
            try (ResultSet datos = consulta.executeQuery()) {
                if (datos.next()) {
                    return crearUsuario(datos);
                }
            }
        }
        return null;
    }

    /**
     * Valida el login: devuelve el usuario si la cedula y la contrasena
     * coinciden con un registro de la tabla usuario; si no, devuelve null.
     */
    public Usuario validarLogin(String cedula, String contrasena) throws SQLException {
        String sql = "SELECT cedula, nombre, contrasena FROM usuario "
                   + "WHERE cedula = ? AND contrasena = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cedula);
            consulta.setString(2, contrasena);
            try (ResultSet datos = consulta.executeQuery()) {
                if (datos.next()) {
                    return crearUsuario(datos);
                }
            }
        }
        return null;
    }

    private Usuario crearUsuario(ResultSet datos) throws SQLException {
        return new Usuario(
            datos.getString("cedula"),
            datos.getString("nombre"),
            datos.getString("contrasena")
        );
    }
}