package clienteescritoriocupones;

import clienteescritoriocupones.interfas.IRespuesta;
import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
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

public class FXMLAdminEmpresaController implements Initializable {
    
    private ObservableList<Empresa> empresas;
    private int idEmpresa;
    private int idUsuario;
    private String nombreEmpresa;
    
    @FXML
    private AnchorPane lbTitulo;
    @FXML
    private TableView<Empresa> tvEmpresa;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colNombreComercial;
    @FXML
    private TableColumn colNombreR;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colPaginaWeb;
    @FXML
    private TableColumn colRfc;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TextField tfBuscarEmpresa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        empresas = FXCollections.observableArrayList();
        configurarTabla();
        //buscarEmpresa();
    }
    
    public void inicializarInformacion(Integer idUsuario){
        System.out.println("ID de usuario recibido: " + idUsuario);
        this.idUsuario = idUsuario;
        descargarEmpresas();
        inicializarBusquedaEmpresa();
    }
    
    private void configurarTabla(){
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory("nombreEmpresa"));
        colNombreComercial.setCellValueFactory(new PropertyValueFactory("nombreComercial"));
        colNombreR.setCellValueFactory(new PropertyValueFactory("nombreRepresentante"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colPaginaWeb.setCellValueFactory(new PropertyValueFactory("paginaWeb"));
        colRfc.setCellValueFactory(new PropertyValueFactory("rfc"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
    }
    
    private void descargarEmpresas(){
        HashMap<String, Object> respuesta = EmpresaDAO.obtenerEmpresaByUsuario(idUsuario);
        if(!(boolean) respuesta.get("error")){
            List<Empresa> listaWS = (List<Empresa>) respuesta.get("empresa");
            empresas.addAll(listaWS);
            tvEmpresa.setItems(empresas);
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
    private void btnFormularioEmpresa(ActionEvent event) {
       irFormularioEmpresa(null, idEmpresa);
       actualizarTabla();
    }
    
    private void irFormularioEmpresa(Empresa empresa, int idEmpresa){
         try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLFormularioEmpresa.fxml"));
            Parent vista = loadVista.load();
            
            FXMLFormularioEmpresaController formularioEmpresaController = loadVista.getController();
            formularioEmpresaController.inicializarEmpresa(empresa, idEmpresa);
            
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Formulario empresas");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnEditarEmpresa(ActionEvent event) {
        int posicionSeleccionada = tvEmpresa.getSelectionModel().getSelectedIndex();
        if(posicionSeleccionada != -1){
            Empresa empresa = empresas.get(posicionSeleccionada);
            irFormularioEmpresa(empresa, idEmpresa);
            actualizarTabla();
        }else{
            Utilidades.mostrarAlertaSimple("Selección de empresa", "Para poder modificar debes seleccionar una empresa de la tabla", Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void btnEliminarEmpresa(ActionEvent event) {
        Empresa empresaSeleccionada = tvEmpresa.getSelectionModel().getSelectedItem();
        if(empresaSeleccionada != null){
            Optional<ButtonType> respuesta = Utilidades.mostrarAlertaConfirmacion("Confirmar eliminación", 
                    "¿Estás seguro de eliminar la empresa " + empresaSeleccionada.getNombreEmpresa() + " de su registro?");
            if(respuesta.get() == ButtonType.OK){
                eliminarEmpresa(empresaSeleccionada.getIdEmpresa());
                tvEmpresa.getItems().remove(empresaSeleccionada);
                
                tvEmpresa.setItems(null);
                tvEmpresa.layout();
                tvEmpresa.setItems(empresas);
                
                //tvEmpresa.getItems().removeIf(empresa -> empresa.getIdEmpresa() == idEmpresa);
            }
        }else{
            Utilidades.mostrarAlertaSimple("Selección de empresa", "Por favor, seleccione una empresa para eliminar", Alert.AlertType.WARNING);
        }
    }
    
    private void eliminarEmpresa(Integer idEmpresa){
        Mensaje msj = EmpresaDAO.eliminarEmpresa(idEmpresa);
    }
    
    private void actualizarTabla(){
        empresas.clear();
        descargarEmpresas();
    }
    
    /*
    private void buscarEmpresa(){
        tfBuscarEmpresa.setOnKeyReleased((e) -> {
            String nombreEmpresa = tfBuscarEmpresa.getText();
            if(nombreEmpresa.isEmpty()){
                empresas.clear();
                descargarEmpresas();
            } else {
                empresas.clear();
                List<Empresa> empresasEncontradas = EmpresaDAO.consumirServicioBusqueda(nombreEmpresa);
                empresas.addAll(empresasEncontradas);
                tvEmpresa.setItems(empresas);
            }
        });
    }*/
    
    private void inicializarBusquedaEmpresa(){
        if(empresas != null){
            FilteredList<Empresa> filtroEmpresa = new FilteredList<>(empresas, p -> true);
            tfBuscarEmpresa.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    filtroEmpresa.setPredicate(empresaBusqueda -> {
                        //Regla cuando es vacío
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String lowerFilter = newValue.toLowerCase();
                        //Reglas de filtrado
                        if(empresaBusqueda.getNombreEmpresa().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        if(empresaBusqueda.getRfc().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        if(empresaBusqueda.getNombreRepresentante().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Empresa> empresasOrdenados = new SortedList<>(filtroEmpresa);
            empresasOrdenados.comparatorProperty().bind(tvEmpresa.comparatorProperty());
            tvEmpresa.setItems(empresasOrdenados);
        }
    }
}
