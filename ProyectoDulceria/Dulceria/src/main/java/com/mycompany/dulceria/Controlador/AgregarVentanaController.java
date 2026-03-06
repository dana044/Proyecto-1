package com.mycompany.dulceria.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.mycompany.dulceria.Modelo.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controlador JavaFX para la ventana de Agregar Producto.
 * Incluye validaciones completas para todos los campos.
 * 
 * @author Dana, Rubi, Citlaly
 * @version 2.0 (JavaFX)
 */
public class AgregarVentanaController {
    
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecioCompra;
    @FXML private TextField txtPrecioVenta;
    @FXML private TextField txtMarca;
    @FXML private TextField txtFechaCaducidad;
    @FXML private TextField txtCantidad;
    @FXML private Button btnAgregar;
    @FXML private Button btnRegresar;
    
    private ImpDulceDAO dao;
    
    public void setDao(ImpDulceDAO dao) {
        this.dao = dao;
    }
    
    @FXML
    private void initialize() {
        // Configuraciones iniciales
    }
    
    @FXML
    private void agregarProducto() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String precioCompra = txtPrecioCompra.getText().trim();
        String precioVenta = txtPrecioVenta.getText().trim();
        String marca = txtMarca.getText().trim();
        String fecha = txtFechaCaducidad.getText().trim();
        String cantidad = txtCantidad.getText().trim();
        
        // Validaciones
        if (!validarCodigo(id)) {
            mostrarAlerta("ID inválido", "El ID debe ser numérico y tener máximo 10 dígitos.", Alert.AlertType.WARNING);
            return;
        }
        
        if (!validarNombre(nombre)) {
            mostrarAlerta("Nombre inválido", "El nombre solo puede contener letras y espacios.", Alert.AlertType.WARNING);
            return;
        }
        
        String validacionFecha = validarFecha(fecha);
        if (!validacionFecha.equals("fecha valida")) {
            mostrarAlerta("Fecha inválida", validacionFecha, Alert.AlertType.WARNING);
            return;
        }
        
        if (!precioCompra.matches("\\d+(\\.\\d+)?")) {
            mostrarAlerta("Precio inválido", "El precio de compra debe ser un número válido.", Alert.AlertType.WARNING);
            return;
        }
        
        if (!precioVenta.matches("\\d+(\\.\\d+)?")) {
            mostrarAlerta("Precio inválido", "El precio de venta debe ser un número válido.", Alert.AlertType.WARNING);
            return;
        }
        
        if (marca.isEmpty()) {
            mostrarAlerta("Marca inválida", "La marca no puede estar vacía.", Alert.AlertType.WARNING);
            return;
        }
        
        if (!cantidad.matches("\\d+")) {
            mostrarAlerta("Cantidad inválida", "La cantidad debe ser un número entero.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            Dulce nuevo = new Dulce(id, nombre, precioCompra, precioVenta, marca, fecha, cantidad);
            dao.agregarDulce(nuevo);
            mostrarAlerta("Éxito", "Dulce agregado correctamente", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception ex) {
            mostrarAlerta("Error", "Error al guardar: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
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
    
    private boolean validarCodigo(String codigo) {
        return codigo.matches("\\d{1,10}");
    }
    
    private boolean validarNombre(String nombre) {
        return nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }
    
    private String validarFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return "La fecha no puede estar vacía";
        }
        
        try {
            LocalDate date = LocalDate.parse(fecha, DateTimeFormatter.ISO_LOCAL_DATE);
            if (date.isBefore(LocalDate.now())) {
                return "La fecha de caducidad debe ser posterior a hoy";
            }
            return "fecha valida";
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(fecha, formatter);
                if (date.isBefore(LocalDate.now())) {
                    return "La fecha de caducidad debe ser posterior a hoy";
                }
                return "fecha valida";
            } catch (DateTimeParseException ex) {
                return "Formato de fecha inválido. Use YYYY-MM-DD o DD/MM/YYYY";
            }
        }
    }
    
    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtPrecioCompra.clear();
        txtPrecioVenta.clear();
        txtMarca.clear();
        txtFechaCaducidad.clear();
        txtCantidad.clear();
        txtId.requestFocus();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
