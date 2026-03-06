package com.mycompany.dulceria.Controlador;

import com.mycompany.dulceria.Modelo.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para el Menú Principal del Administrador en JavaFX.
 * Gestiona el acceso a todas las secciones del sistema.
 */
public class MenuAdminController implements Initializable {

    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnMostrar;
    @FXML private Button btnActualizar;
    @FXML private Button btnCrearUsuario;
    @FXML private Button btnRegistrarVenta;
    @FXML private Button btnReporte;
    @FXML private Button btnHistorial;
    @FXML private Button btnSalir;
    @FXML private Button btnRegresar;

    private ImpDulceDAO dao;
    private ImpVentaDAO ventaDao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            dao = new ImpDulceDAO();
            ventaDao = new ImpVentaDAO();
        } catch (Exception ex) {
            mostrarAlerta("Error", "Error cargando base de datos: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAgregar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/AgregarVentana.fxml"));
        Parent root = loader.load();
        AgregarVentanaController ctrl = loader.getController();
        ctrl.setDao(dao);
        Historial.registrar("Entró a 'Agregar producto'");
        cambiarEscena(root, btnAgregar);
    }

    @FXML
    private void handleEliminar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/EliminarVentana.fxml"));
        Parent root = loader.load();
        EliminarVentanaController ctrl = loader.getController();
        ctrl.setDao(dao);
        Historial.registrar("Entró a 'Eliminar producto'");
        cambiarEscena(root, btnEliminar);
    }

    @FXML
    private void handleMostrar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/MostrarActualizarVentana.fxml"));
        Parent root = loader.load();
        MostrarActualizarController ctrl = loader.getController();
        ctrl.setDao(dao);
        Historial.registrar("Consultó información de inventario");
        cambiarEscena(root, btnMostrar);
    }

    @FXML
    private void handleActualizar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/VentanaActualizar.fxml"));
        Parent root = loader.load();
        VentanaActualizarController ctrl = loader.getController();
        ctrl.setDao(dao);
        Historial.registrar("Entró a 'Actualizar producto'");
        cambiarEscena(root, btnActualizar);
    }

    @FXML
    private void handleCrearUsuario() throws IOException {
        // CrearUsuarioController crea su propio dao internamente en initialize()
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/CrearUsuarioVentana.fxml"));
        Parent root = loader.load();
        Historial.registrar("Entró a 'Crear usuario'");
        cambiarEscena(root, btnCrearUsuario);
    }

    @FXML
    private void handleRegistrarVenta() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/RegistrarVentana.fxml"));
        Parent root = loader.load();
        RegistrarVentanaController ctrl = loader.getController();
        ctrl.setDao(dao);
        ctrl.setVentaDao(ventaDao);
        Historial.registrar("Entró a 'Registrar venta'");
        cambiarEscena(root, btnRegistrarVenta);
    }

    @FXML
    private void handleReporte() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/ReporteVentana.fxml"));
        Parent root = loader.load();
        ReporteVentanaController ctrl = loader.getController();
        ctrl.setVentaDao(ventaDao);
        Historial.registrar("Consultó el reporte de ventas");
        cambiarEscena(root, btnReporte);
    }

    @FXML
    private void handleHistorial() {
        mostrarAlerta("Historial del Sistema", Historial.verHistorial(), Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleRegresar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/LoginVentana.fxml"));
        Parent root = loader.load();
        cambiarEscena(root, btnRegresar);
    }

    @FXML
    private void handleSalir() {
        System.exit(0);
    }

    /**
     * Cambia la escena usando un root ya cargado con su controlador configurado.
     */
    private void cambiarEscena(Parent root, Button botonReferencia) {
        Stage stage = (Stage) botonReferencia.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}