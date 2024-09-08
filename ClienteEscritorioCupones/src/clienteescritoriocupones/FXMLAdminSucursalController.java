package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.SucursalDAO;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Sucursal;
import clienteescritoriocupones.modelo.pojo.Usuario;
import clienteescritoriocupones.utils.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLAdminSucursalController implements Initializable {
    
    private ObservableList<Sucursal> sucursales;
    private int idSucursal;
    private int idUsuario;
    
    @FXML
    private AnchorPane lbTitulo;
    @FXML
    private TableView<Sucursal> tvSucursal;
    @FXML
    private TableColumn colNombreSucursal;
    @FXML
    private TableColumn colNombreEncargado;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TextField tfBuscarSucursal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sucursales = FXCollections.observableArrayList();
        configurarTabla();
        //buscarSucursal();
    }
    
    public void inicializarInformacion(Integer idUsuario){
        System.out.println("ID de Empresa recibido: " + idUsuario);
        this.idUsuario = idUsuario;
        descargarSucursal();
        inicializarBusquedaSucursal();
    }
    
    private void configurarTabla(){
        colNombreSucursal.setCellValueFactory(new PropertyValueFactory("nombreSucursal"));
        colNombreEncargado.setCellValueFactory(new PropertyValueFactory("nombreEncargado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory("nombreEmpresa"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
    }
    
    private void descargarSucursal(){
        HashMap<String, Object> respuesta = SucursalDAO.obtenerEmpresaSucursalByUsuario(idUsuario);
        if(!(boolean) respuesta.get("error")){
            List<Sucursal> listaWS = (List<Sucursal>) respuesta.get("sucursal");
            sucursales.addAll(listaWS);
            tvSucursal.setItems(sucursales);
        }else{
            Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnHome(ActionEvent event) {
        Stage stageActual = (Stage) lbTitulo.getScene().getWindow();
        try {
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource("FXMLHome.fxml"));
            Parent vista = loadMain.load();
            
            stageActual.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnAgregarSucursal(ActionEvent event) {
        irFormularioSucursal(null, idSucursal);
        actualizarTabla();
    }
    
    private void irFormularioSucursal(Sucursal sucursal, Integer idSucursal){
         try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLFormularioSucursal.fxml"));
            Parent vista = loadVista.load();
            
            FXMLFormularioSucursalController controller = loadVista.getController();
            controller.inicializarInformacion(sucursal, idSucursal);
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Agregar sucursales");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnEditarSucursal(ActionEvent event) {
        int posicionSeleccionada = tvSucursal.getSelectionModel().getSelectedIndex();
        if(posicionSeleccionada != -1){
            Sucursal sucursal = sucursales.get(posicionSeleccionada);
            irFormularioSucursal(sucursal, idSucursal);
            actualizarTabla();
        }else{
            Utilidades.mostrarAlertaSimple("Selección de sucursal", "Para poder modificar debes seleccionar una sucursal de la tabla", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminarSucursal(ActionEvent event) {
        Sucursal sucursalSeleccionada = tvSucursal.getSelectionModel().getSelectedItem();
        if(sucursalSeleccionada != null){
            Optional<ButtonType> respuesta = Utilidades.mostrarAlertaConfirmacion("Confirmar eliminación", 
                    "¿Estás seguro de eliminar la sucursal " + sucursalSeleccionada.getNombreSucursal() + " de su registro?");
            if(respuesta.get() == ButtonType.OK){
                eliminarSucursal(sucursalSeleccionada.getIdSucursal());
                tvSucursal.getItems().remove(sucursalSeleccionada);
                
                tvSucursal.setItems(null);
                tvSucursal.layout();
                tvSucursal.setItems(sucursales);
            }
        }else{
            Utilidades.mostrarAlertaSimple("Selección de sucursal", "Por favor, seleccione una sucursal para eliminar", Alert.AlertType.WARNING);
        }
    }
    
    private void eliminarSucursal(Integer idSucursal){
        Mensaje msj = SucursalDAO.eliminarSucursal(idSucursal);
    }
    
    private void actualizarTabla(){
        sucursales.clear();
        descargarSucursal();
    }
    
    private void inicializarBusquedaSucursal(){
        if(sucursales != null){
            FilteredList<Sucursal> filtroSucursal = new FilteredList<>(sucursales, p -> true);
            tfBuscarSucursal.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    filtroSucursal.setPredicate(sucursalBusqueda -> {
                        //Regla cuando es vacío
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String lowerFilter = newValue.toLowerCase();
                        //Reglas de filtrado
                        if(sucursalBusqueda.getNombreSucursal().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Sucursal> sucursalesOrdenados = new SortedList<>(filtroSucursal);
            sucursalesOrdenados.comparatorProperty().bind(tvSucursal.comparatorProperty());
            tvSucursal.setItems(sucursalesOrdenados);
        }
    }
    
    /*
    private void buscarSucursal(){
        tfBuscarSucursal.setOnKeyReleased((e) -> {
            String nombreSucursal = tfBuscarSucursal.getText();
            if(nombreSucursal.isEmpty()){
                sucursales.clear();
                descargarSucursal();
            } else {
                sucursales.clear();
                List<Sucursal> sucursalesEncontradas = SucursalDAO.consumirServicioBusqueda(nombreSucursal);
                sucursales.addAll(sucursalesEncontradas);
                tvSucursal.setItems(sucursales);
            }
        });
    }*/
}
