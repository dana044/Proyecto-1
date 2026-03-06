package com.mycompany.dulceria.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.mycompany.dulceria.Modelo.*;

/**
 * Controlador JavaFX para la ventana de Actualización de Productos.
 * Permite buscar un dulce por ID, visualizar sus datos y modificar campos
 * específicos (Precio de compra, Precio de venta, Cantidad).
 * 
 * @author Dana, Rubi, Citlaly
 * @version 2.0 (JavaFX)
 */
public class VentanaActualizarController {
    
    @FXML private TextField txtIdBuscar;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecioCompra;
    @FXML private TextField txtPrecioVenta;
    @FXML private TextField txtMarca;
    @FXML private TextField txtFechaCaducidad;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNuevoValor;
    
    @FXML private Button btnBuscar;
    @FXML private Button btnActualizar;
    @FXML private Button btnRegresar;
    
    @FXML private ComboBox<String> cmbCampo;
    @FXML private Label lblNuevoValor;
    
    /**
     * Data Access Object para gestión de dulces
     */
    private ImpDulceDAO dao;
    
    /**
     * Dulce actualmente cargado
     */
    private Dulce dulceActual;
    
    /**
     * Establece el DAO de dulces.
     * 
     * @param dao La instancia del DAO de dulces
     */
    public void setDao(ImpDulceDAO dao) {
        this.dao = dao;
    }
    
    /**
     * Se ejecuta automáticamente al cargar el FXML.
     * Configura los campos no editables y selecciona la primera opción del ComboBox.
     */
    @FXML
    private void initialize() {
        // Configurar campos no editables
        txtNombre.setEditable(false);
        txtPrecioCompra.setEditable(false);
        txtPrecioVenta.setEditable(false);
        txtMarca.setEditable(false);
        txtFechaCaducidad.setEditable(false);
        txtCantidad.setEditable(false);
        
        // Seleccionar primera opción del ComboBox
        cmbCampo.getSelectionModel().selectFirst();
        lblNuevoValor.setText("Nuevo precio de compra:");
    }
    
    /**
     * Busca un producto por ID y muestra sus datos.
     */
    @FXML
    private void buscarProducto() {
        String id = txtIdBuscar.getText().trim();
        
        if (id.isEmpty()) {
            mostrarAlerta("Advertencia", 
                        "Ingrese el ID del dulce", 
                        Alert.AlertType.WARNING);
            return;
        }
        
        try {
            dulceActual = dao.obtenerDulceDesdeArbol(id);
            
            if (dulceActual == null) {
                mostrarAlerta("No encontrado", 
                            "No se encontró el dulce con ID: " + id, 
                            Alert.AlertType.WARNING);
                limpiarCampos();
                return;
            }
            
            // Mostrar datos del dulce
            txtNombre.setText(dulceActual.getNombre());
            txtPrecioCompra.setText(dulceActual.getPrecioCompra());
            txtPrecioVenta.setText(dulceActual.getPrecioVenta());
            txtMarca.setText(dulceActual.getMarca());
            txtFechaCaducidad.setText(dulceActual.getFecha_caducidad());
            txtCantidad.setText(dulceActual.getCantidad());
            
            // Limpiar campo de nuevo valor
            txtNuevoValor.clear();
            
        } catch (Exception e) {
            mostrarAlerta("Error", 
                        "Error al buscar producto: " + e.getMessage(), 
                        Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Actualiza el campo seleccionado del producto con el nuevo valor.
     */
    @FXML
    private void actualizarProducto() {
        String id = txtIdBuscar.getText().trim();
        String campo = cmbCampo.getValue();
        String nuevoValor = txtNuevoValor.getText().trim();
        
        // Validaciones
        if (id.isEmpty()) {
            mostrarAlerta("Advertencia", 
                        "Primero busca un producto", 
                        Alert.AlertType.WARNING);
            return;
        }
        
        if (nuevoValor.isEmpty()) {
            mostrarAlerta("Advertencia", 
                        "Ingrese el nuevo valor", 
                        Alert.AlertType.WARNING);
            return;
        }
        
        if (dulceActual == null) {
            mostrarAlerta("Advertencia", 
                        "No hay un producto cargado", 
                        Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Actualizar el campo 
            switch (campo) {
                case "Precio de compra":
                    // Validar que sea un número
                    try {
                        Double.parseDouble(nuevoValor);
                        dulceActual.setPrecioCompra(nuevoValor);
                        txtPrecioCompra.setText(nuevoValor);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", 
                                    "El precio debe ser un número válido", 
                                    Alert.AlertType.ERROR);
                        return;
                    }
                    break;
                    
                case "Precio de venta":
                    // Validar que sea un número
                    try {
                        Double.parseDouble(nuevoValor);
                        dulceActual.setPrecioVenta(nuevoValor);
                        txtPrecioVenta.setText(nuevoValor);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", 
                                    "El precio debe ser un número válido", 
                                    Alert.AlertType.ERROR);
                        return;
                    }
                    break;
                    
                case "Cantidad":
                    // Validar que sea un número entero
                    try {
                        Integer.parseInt(nuevoValor);
                        dulceActual.setCantidad(nuevoValor);
                        txtCantidad.setText(nuevoValor);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", 
                                    "La cantidad debe ser un número entero válido", 
                                    Alert.AlertType.ERROR);
                        return;
                    }
                    break;
            }
            
            // Guardar cambios en el DAO
            dao.actualizarDulce(dulceActual);
            
            mostrarAlerta("Éxito", 
                        "Producto actualizado correctamente", 
                        Alert.AlertType.INFORMATION);
            
            // Limpiar campo de nuevo valor
            txtNuevoValor.clear();
            
        } catch (Exception ex) {
            mostrarAlerta("Error", 
                        "Error al guardar cambios: " + ex.getMessage(), 
                        Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el cambio de selección en el ComboBox.
     * Actualiza la etiqueta para indicar qué valor se espera.
     */
    @FXML
    private void cambiarCampo() {
        String campo = cmbCampo.getValue();
        
        if (campo != null) {
            switch (campo) {
                case "Precio de compra":
                    lblNuevoValor.setText("Nuevo precio de compra:");
                    break;
                case "Precio de venta":
                    lblNuevoValor.setText("Nuevo precio de venta:");
                    break;
                case "Cantidad":
                    lblNuevoValor.setText("Nueva cantidad:");
                    break;
            }
            
            // Limpiar campo de nuevo valor al cambiar de campo
            txtNuevoValor.clear();
            txtNuevoValor.requestFocus();
        }
    }
    
    /**
     * Regresa al menú de administrador.
     */
    @FXML
    private void regresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/MenuAdmin.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", 
                        "Error al regresar: " + e.getMessage(), 
                        Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Limpia todos los campos de datos del producto.
     */
    private void limpiarCampos() {
        txtNombre.clear();
        txtPrecioCompra.clear();
        txtPrecioVenta.clear();
        txtMarca.clear();
        txtFechaCaducidad.clear();
        txtCantidad.clear();
        txtNuevoValor.clear();
        dulceActual = null;
    }
    
    /**
     * Muestra una alerta al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
