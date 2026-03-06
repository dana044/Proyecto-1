package com.mycompany.dulceria.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.mycompany.dulceria.Modelo.*;
import java.util.ArrayList;

public class ReporteVentanaController {

    @FXML private TextField txtFechaBuscada;
    @FXML private Button btnBuscar;
    @FXML private Button btnMostrarTodas;
    @FXML private Button btnRegresar;

    @FXML private TableView<VentaReporte> tablaVentas;
    @FXML private TableColumn<VentaReporte, String> colIdVenta;
    @FXML private TableColumn<VentaReporte, String> colFecha;
    @FXML private TableColumn<VentaReporte, String> colTotal;

    private ImpVentaDAO ventaDao;
    private ObservableList<VentaReporte> listaVentas = FXCollections.observableArrayList();

    public void setVentaDao(ImpVentaDAO ventaDao) {
        this.ventaDao = ventaDao;
        cargarTodasLasVentas();
    }

    @FXML
    private void initialize() {
        // Lambdas en lugar de PropertyValueFactory — no dependen de reflexión
        colIdVenta.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdVenta()));
        colFecha.setCellValueFactory(data   -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFecha()));
        colTotal.setCellValueFactory(data   -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTotal()));

        tablaVentas.setItems(listaVentas);
        tablaVentas.setPlaceholder(new Label("No hay ventas registradas"));
    }

    @FXML
    private void buscarPorFecha() {
        String fecha = txtFechaBuscada.getText().trim();
        if (fecha.isEmpty()) {
            mostrarAlerta("Advertencia", "Por favor ingresa una fecha (YYYY-MM-DD)", Alert.AlertType.WARNING);
            return;
        }
        try {
            ArrayList<Venta> ventas = ventaDao.ventasPorFecha(fecha);
            listaVentas.clear();
            if (ventas.isEmpty()) {
                mostrarAlerta("Sin resultados", "No se encontraron ventas para: " + fecha, Alert.AlertType.INFORMATION);
            } else {
                for (Venta v : ventas)
                    listaVentas.add(new VentaReporte(v.getIdVenta(), v.getFecha(), v.getTotal()));
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al buscar ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void mostrarTodasLasVentas() {
        txtFechaBuscada.clear();
        cargarTodasLasVentas();
    }

    @FXML
    private void regresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/MenuAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al regresar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarTodasLasVentas() {
        if (ventaDao == null) return;
        try {
            ArrayList<Venta> ventas = ventaDao.obtenerVentas();
            listaVentas.clear();
            for (Venta v : ventas)
                listaVentas.add(new VentaReporte(v.getIdVenta(), v.getFecha(), v.getTotal()));
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public static class VentaReporte {
        private String idVenta, fecha, total;

        public VentaReporte(String idVenta, String fecha, String total) {
            this.idVenta = idVenta; this.fecha = fecha; this.total = total;
        }

        public String getIdVenta() { return idVenta; }
        public String getFecha()   { return fecha; }
        public String getTotal()   { return total; }
    }
}