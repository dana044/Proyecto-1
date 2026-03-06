package com.mycompany.dulceria.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.mycompany.dulceria.Modelo.*;

/**
 * Controlador JavaFX para la ventana de Eliminar Producto.
 * Permite reducir la cantidad de stock de un producto o eliminarlo completamente.
 * 
 * @author Dana, Rubi, Citlaly
 * @version 2.0 (JavaFX)
 */
public class EliminarVentanaController {
    
    @FXML private TextField txtCodigo;
    @FXML private TextField txtCantidad;
    @FXML private Button btnEliminar;
    @FXML private Button btnRegresar;
    
    private ImpDulceDAO dao;
    
    public void setDao(ImpDulceDAO dao) {
        this.dao = dao;
    }
    
    @FXML
    private void initialize() {
    }
    
    @FXML
    private void eliminarProducto() {
        String id = txtCodigo.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();
        
        if (id.isEmpty()) {
            mostrarAlerta("Advertencia", "Por favor ingresa el código del producto", Alert.AlertType.WARNING);
            return;
        }
        
        if (cantidadStr.isEmpty()) {
            mostrarAlerta("Advertencia", "Por favor ingresa la cantidad a eliminar", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            
            if (cantidad <= 0) {
                mostrarAlerta("Error", "La cantidad debe ser mayor a 0", Alert.AlertType.ERROR);
                return;
            }
            
            String resultado = dao.restarCantidad(id, cantidad);
            mostrarAlerta("Resultado", resultado, Alert.AlertType.INFORMATION);
            limpiarCampos();
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La cantidad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al eliminar producto: " + e.getMessage(), Alert.AlertType.ERROR);
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
    
    private void limpiarCampos() {
        txtCodigo.clear();
        txtCantidad.clear();
        txtCodigo.requestFocus();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
