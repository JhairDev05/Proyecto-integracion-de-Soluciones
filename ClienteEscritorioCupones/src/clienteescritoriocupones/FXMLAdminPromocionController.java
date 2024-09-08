package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.CatalogoDAO;
import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.PromocionDAO;
import clienteescritoriocupones.modelo.dao.UsuarioDAO;
import clienteescritoriocupones.modelo.pojo.Categoria;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Estatus;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Promocion;
import clienteescritoriocupones.modelo.pojo.Sucursal;
import clienteescritoriocupones.modelo.pojo.TipoPromo;
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
import javafx.util.Callback;

public class FXMLAdminPromocionController implements Initializable {

    private ObservableList<Promocion> promociones;
    private int idUsuario;
    private int idPromocion;
    
    @FXML
    private AnchorPane lbTitulo;
    @FXML
    private TableView<Promocion> tvPromociones;
    @FXML
    private TableColumn colNombreP;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaFin;
    @FXML
    private TableColumn colRestricciones;
    @FXML
    private TableColumn colCodigoP;
    @FXML
    private TableColumn colTipoPromo;
    @FXML
    private TableColumn colCategoria;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TextField tfBuscarPromo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        promociones = FXCollections.observableArrayList();
        configurarTabla();
        //buscarPromocion();
    }
    
    public void inicializarInformacion(Integer idUsuario){
        this.idUsuario = idUsuario;
        descargarPromocion();
        inicializarBusquedaPromocion();
    }
    
    private void configurarTabla(){
        colNombreP.setCellValueFactory(new PropertyValueFactory("nombrePromo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicioPromo"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory("fechaFinPromo"));
        colRestricciones.setCellValueFactory(new PropertyValueFactory("restricciones"));
        colCodigoP.setCellValueFactory(new PropertyValueFactory("codigoPromocion"));
        colTipoPromo.setCellValueFactory(new PropertyValueFactory("nombrePromocion"));
        colCategoria.setCellValueFactory(new PropertyValueFactory("nombreCategoria"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
    }
    
    private void descargarPromocion(){
        HashMap<String, Object> respuesta = PromocionDAO.obtenerPromoEmpresaByUsuario(idUsuario);
        if(!(boolean) respuesta.get("error")){
            List<Promocion> listaWS = (List<Promocion>) respuesta.get("promocion");
            promociones.addAll(listaWS);
            tvPromociones.setItems(promociones);
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
    private void btnAgregarPromocion(ActionEvent event) {
        irFormularioPromocion(null, idPromocion);
        actualizarTabla();
    }
    
    private void irFormularioPromocion(Promocion promocion, Integer idPromocion){
        try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLFormularioPromocion.fxml"));
            Parent vista = loadVista.load();
            
            FXMLFormularioPromocionController controller = loadVista.getController();
            controller.inicializarInformacion(promocion, idPromocion);
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Agregar promociones");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnEditarPromocion(ActionEvent event) {
        int posicionSeleccionada = tvPromociones.getSelectionModel().getSelectedIndex();
        if(posicionSeleccionada != -1){
            Promocion promocion = promociones.get(posicionSeleccionada);
            irFormularioPromocion(promocion, idPromocion);
            actualizarTabla();
        }else{
            Utilidades.mostrarAlertaSimple("Selección de promoción", "Para poder modificar debes seleccionar una promoción de la tabla", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminarPromocion(ActionEvent event) {
        Promocion promocionSeleccionada = tvPromociones.getSelectionModel().getSelectedItem();
        if(promocionSeleccionada != null){
            Optional<ButtonType> respuesta = Utilidades.mostrarAlertaConfirmacion("Confirmar eliminación", 
                    "¿Estás seguro de eliminar la promoción " + promocionSeleccionada.getNombrePromo()+ " de su registro?");
            if(respuesta.get() == ButtonType.OK){
                eliminarPromocion(promocionSeleccionada.getIdPromocion());
                //tvPromociones.getItems().remove(promocionSeleccionada);
                //tvPromociones.getItems().remove(promocionSeleccionada);
                
                //tvPromociones.setItems(null);
                //tvPromociones.layout();
                //tvPromociones.setItems(promociones);
            }
        }else{
            Utilidades.mostrarAlertaSimple("Selección de sucursal", "Por favor, seleccione una sucursal para eliminar", Alert.AlertType.WARNING);
        }
    }
    
    private void eliminarPromocion(Integer idPromocion){
        Mensaje msj = PromocionDAO.eliminarPromocion(idPromocion);
        //tvPromociones.getItems().removeIf(promocion -> promocion.getIdPromocion() == idPromocion);
    }
    
    private void actualizarTabla(){
        promociones.clear();
        descargarPromocion();
    }
    
    private void inicializarBusquedaPromocion(){
        if(promociones != null){
            FilteredList<Promocion> filtroPromocion = new FilteredList<>(promociones, p -> true);
            tfBuscarPromo.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    filtroPromocion.setPredicate(promocionBusqueda -> {
                        //Regla cuando es vacío
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String lowerFilter = newValue.toLowerCase();
                        //Reglas de filtrado
                        if(promocionBusqueda.getFechaInicioPromo().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        if(promocionBusqueda.getFechaFinPromo().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        if(promocionBusqueda.getNombrePromo().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Promocion> promocionesOrdenados = new SortedList<>(filtroPromocion);
            promocionesOrdenados.comparatorProperty().bind(tvPromociones.comparatorProperty());
            tvPromociones.setItems(promocionesOrdenados);
        }
    }
}
    /*
    private void buscarPromocion(){
        tfBuscarPromo.setOnKeyReleased((e) -> {
            String nombrePromo = tfBuscarPromo.getText();
            if(nombrePromo.isEmpty()){
                promociones.clear();
                descargarPromocion();
            } else {
                promociones.clear();
                List<Promocion> promocionesEncontradas = PromocionDAO.consumirServicioBusqueda(nombrePromo);
                promociones.addAll(promocionesEncontradas);
                tvPromociones.setItems(promociones);
            }
        });
    }*/
    
    
    

