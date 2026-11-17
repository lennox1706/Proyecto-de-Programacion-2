/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.facturacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lennox
 */
public class ConexionBD {

    private ConexionBD() {    
    }

    public static Connection obtenerConexion()
            throws SQLException { 
        return DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USUARIO, ConfiguracionBD.PASSWORD);
    }
}

