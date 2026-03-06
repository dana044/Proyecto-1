package com.mycompany.dulceria.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.mycompany.dulceria.Modelo.*;
import java.util.ArrayList;

/**
 * Controlador JavaFX para la ventana de Mostrar/Actualizar Inventario.
 * Maneja la visualización del inventario de dulces y los diferentes
 * algoritmos de ordenamiento (Quick Sort, Heap Sort, Merge Sort).
 * 
 * @author Dana, Rubi, Citlaly
 * @version 2.0 (JavaFX)
 */
public class MostrarActualizarController {
    
    @FXML
    private TextArea textAreaProductos;
    
    @FXML
    private Button btnQuick;
    
    @FXML
    private Button btnHeap;
    
    @FXML
    private Button btnMerge;
    
    @FXML
    private Button btnRegresar;
    
    /**
     * Data Access Object (DAO) para la gestión de productos (dulces).
     */
    private ImpDulceDAO dao;
    
    /**
     * Establece el DAO de dulces y carga el inventario inicial.
     * Este método debe ser llamado después de cargar el FXML.
     * 
     * @param dao La instancia del DAO de dulces
     */
    public void setDao(ImpDulceDAO dao) {
        this.dao = dao;
        cargarInventario();
    }
    
    /**
     * Método que se ejecuta automáticamente después de cargar el FXML.
     * Inicializa los componentes si es necesario.
     */
    @FXML
    private void initialize() {
        // Configuraciones iniciales si son necesarias
        textAreaProductos.setEditable(false);
    }
    
    /**
     * Maneja el evento de clic para el botón Quick Sort.
     * Obtiene la lista actual de dulces del DAO, aplica el algoritmo de 
     * Quick Sort ordenando por nombre, y actualiza el área de texto.
     */
    @FXML
    private void ordenarQuickSort() {
        if (dao == null) {
            textAreaProductos.setText("Error: DAO no inicializado");
            return;
        }
        
        ArrayList<Dulce> lista = dao.obtenerDulces();
        OrdenamientosDulce.quickSort(lista, 0, lista.size() - 1);
        mostrarEnTextArea(lista);
    }
    
    /**
     * Maneja el evento de clic para el botón Heap Sort.
     * Obtiene la lista actual de dulces del DAO, aplica el algoritmo de 
     * Heap Sort ordenando por precio, y actualiza el área de texto.
     */
    @FXML
    private void ordenarHeapSort() {
        if (dao == null) {
            textAreaProductos.setText("Error: DAO no inicializado");
            return;
        }
        
        ArrayList<Dulce> lista = dao.obtenerDulces();
        OrdenamientosDulce.heapSort(lista);
        mostrarEnTextArea(lista);
    }
    
    /**
     * Maneja el evento de clic para el botón Merge Sort.
     * Obtiene la lista actual de dulces del DAO, aplica el algoritmo de 
     * Merge Sort ordenando por ID, y actualiza el área de texto.
     */
    @FXML
    private void ordenarMergeSort() {
        if (dao == null) {
            textAreaProductos.setText("Error: DAO no inicializado");
            return;
        }
        
        ArrayList<Dulce> lista = dao.obtenerDulces();
        OrdenamientosDulce.mergeSort(lista);
        mostrarEnTextArea(lista);
    }
    
    /**
     * Maneja el evento de clic para el botón "Regresar".
     * Cierra la ventana actual y regresa al menú de administrador.
     */
    @FXML
    private void regresarMenuAdmin() {
        try {
            // Cargar el FXML del MenuAdmin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/dulceria/fxml/MenuAdmin.fxml"));
            Parent root = loader.load();
            
            // Obtener el stage actual y cambiar la escena
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            textAreaProductos.setText("Error al regresar al menú: " + e.getMessage());
        }
    }
    
    /**
     * Carga el inventario inicial de dulces en el área de texto.
     * Obtiene todos los dulces del DAO y los muestra sin ordenar.
     */
    private void cargarInventario() {
        if (dao == null) {
            textAreaProductos.setText("Error: DAO no inicializado");
            return;
        }
        
        ArrayList<Dulce> lista = dao.obtenerDulces();
        mostrarEnTextArea(lista);
    }
    
    /**
     * Método auxiliar que toma una lista de objetos Dulce y genera una 
     * cadena formateada para mostrar los detalles de cada dulce en el 
     * área de texto.
     *
     * @param lista La lista de dulces (ordenada o sin ordenar) a mostrar
     */
    private void mostrarEnTextArea(ArrayList<Dulce> lista) {
        StringBuilder sb = new StringBuilder();
        
        for (Dulce d : lista) {
            sb.append("ID: ").append(d.getId()).append("\n");
            sb.append("Nombre: ").append(d.getNombre()).append("\n");
            sb.append("Precio Compra: ").append(d.getPrecioCompra()).append("\n");
            sb.append("Precio Venta: ").append(d.getPrecioVenta()).append("\n");
            sb.append("Marca: ").append(d.getMarca()).append("\n");
            sb.append("Fecha Caducidad: ").append(d.getFecha_caducidad()).append("\n");
            sb.append("Cantidad: ").append(d.getCantidad()).append("\n");
            sb.append("--------------------------------------\n");
        }
        
        textAreaProductos.setText(sb.toString());
    }
}
