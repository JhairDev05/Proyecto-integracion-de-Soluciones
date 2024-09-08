package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.CuponDAO;
import clienteescritoriocupones.modelo.pojo.Cupon;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Promocion;
import clienteescritoriocupones.modelo.pojo.Sucursal;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLAdminCuponController implements Initializable {

    private int idUsuario;
    private int idPromocion;
    private int idCupon;
    private ObservableList<Cupon> cupones;
    
    @FXML
    private TableView<Cupon> tvCupon;
    @FXML
    private TableColumn colNombrePromo;
    @FXML
    private TableColumn colCuponesDisponibles;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colNombreSucursal;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TextField tfBuscarCupon;
    @FXML
    private Button btnAgregarCupon;
    @FXML
    private Button btnEliminarCupon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cupones = FXCollections.observableArrayList();
        configurarTabla();
        btnAgregarCupon.setDisable(true);
        btnEliminarCupon.setDisable(true);
    }
    
    public void inicializarInformacion(Integer idUsuario){
        this.idUsuario = idUsuario;
        
        descargarCupones();
        inicializarBusquedaCupon();
    }
    
    private void configurarTabla(){
        colNombrePromo.setCellValueFactory(new PropertyValueFactory("nombrePromo"));
        colCuponesDisponibles.setCellValueFactory(new PropertyValueFactory("cuponesDisponibles"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory("nombreEmpresa"));
        colNombreSucursal.setCellValueFactory(new PropertyValueFactory("nombreSucursal"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
    }
    
    private void descargarCupones(){
        HashMap<String, Object> respuesta = CuponDAO.obtenerCupones(idCupon);
        if(!(boolean) respuesta.get("error")){
            List<Cupon> listaWS = (List<Cupon>) respuesta.get("cupon");
            cupones.addAll(listaWS);
            tvCupon.setItems(cupones);
        }else{
            Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnHome(ActionEvent event) {
        Stage stageActual = (Stage) tfBuscarCupon.getScene().getWindow();
        try {
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource("FXMLHome.fxml"));
            Parent vista = loadMain.load();
            
            stageActual.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void btnAgregarCupon(ActionEvent event) {
        irFormularioCupon(null, idCupon);
        actualizarTabla();
    }
    
    private void irFormularioCupon(Cupon cupon, Integer idCupon){
         try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLFormularioCupon.fxml"));
            Parent vista = loadVista.load();
            
            FXMLFormularioCuponController controller = loadVista.getController();
            controller.inicializarInformacion(cupon, idCupon);
            
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
    private void btnEditarCupon(ActionEvent event) {
        int posicionSeleccionada = tvCupon.getSelectionModel().getSelectedIndex();
        if(posicionSeleccionada != -1){
            Cupon cupon = cupones.get(posicionSeleccionada);
            irFormularioCupon(cupon, idCupon);
            actualizarTabla();
        }else{
            Utilidades.mostrarAlertaSimple("Selección de cupón", "Para poder modificar debes seleccionar un cupón de la tabla", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminarCupon(ActionEvent event) {
        Cupon cuponSeleccionado = tvCupon.getSelectionModel().getSelectedItem();
        if (cuponSeleccionado != null){
            Optional<ButtonType> respuesta = Utilidades.mostrarAlertaConfirmacion("Confirmar eliminación", 
                    "¿Estás seguro de eliminar el cupón");
            if(respuesta.get() == ButtonType.OK){
                eliminarCupon(cuponSeleccionado.getIdCupon());
                tvCupon.getItems().remove(cuponSeleccionado);
                
                tvCupon.setItems(null);
                tvCupon.layout();
                tvCupon.setItems(cupones);
            }
        }   else{
            Utilidades.mostrarAlertaSimple("Selección de cupón", "Por favor, seleccione un cupón para eliminar.", Alert.AlertType.WARNING);
        }
    }
    
    private void eliminarCupon(int idCupon){
        Mensaje mensaje = CuponDAO.eliminarCupon(idCupon);
    }

    @FXML
    private void btnCanjearCupon(ActionEvent event) {
        int posicionSeleccionada = tvCupon.getSelectionModel().getSelectedIndex();
        if(posicionSeleccionada != -1){
            Cupon cupon = cupones.get(posicionSeleccionada);
            irCanjearCupon(cupon, idPromocion);
            actualizarTabla();
        }else{
            Utilidades.mostrarAlertaSimple("Selección de cupón", "Para poder canjear un cupón debes seleccionarlo antes", Alert.AlertType.WARNING);
        }
    }
    
    private void irCanjearCupon(Cupon cupon, Integer idPromocion){
         try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLCanjearCupon.fxml"));
            Parent vista = loadVista.load();
            
            FXMLCanjearCuponController controller = loadVista.getController();
            controller.inicializarInformacion(cupon, idPromocion);
            //System.out.println("IdPromocion: " + cupon.getIdPromocion());
            //System.out.println("Código:" + cupon.getCodigoPromocion());
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Cupones");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    private void actualizarTabla(){
        cupones.clear();
        descargarCupones();
    }
    
    
    private void inicializarBusquedaCupon(){
        if(cupones != null){
            FilteredList<Cupon> filtroCupon = new FilteredList<>(cupones, p -> true);
            tfBuscarCupon.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    filtroCupon.setPredicate(cuponBusqueda -> {
                        //Regla cuando es vacío
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String lowerFilter = newValue.toLowerCase();
                        //Reglas de filtrado
                        if(cuponBusqueda.getNombrePromo().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        if(cuponBusqueda.getNombreEmpresa().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        if(cuponBusqueda.getNombreSucursal().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Cupon> cuponOrdenados = new SortedList<>(filtroCupon);
            cuponOrdenados.comparatorProperty().bind(tvCupon.comparatorProperty());
            tvCupon.setItems(cuponOrdenados);
        }
    }
}
