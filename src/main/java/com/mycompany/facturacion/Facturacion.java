package com.mycompany.facturacion;

/**
 * Punto de entrada del sistema: siempre inicia mostrando el login.
 */
public class Facturacion {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}