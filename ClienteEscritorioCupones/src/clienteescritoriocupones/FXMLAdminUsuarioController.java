package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.UsuarioDAO;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLAdminUsuarioController implements Initializable {

    private ObservableList<Usuario> usuarios;
    private Integer idUsuario;
    
    @FXML
    private AnchorPane lbTitulo;
    @FXML
    private TableView<Usuario> tvUsuario;
    @FXML
    private TableColumn colNombreUsuario;
    @FXML
    private TableColumn colApellidoP;
    @FXML
    private TableColumn colApellidoM;
    @FXML
    private TableColumn colCurp;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colUsername;
    @FXML
    private TableColumn colRol;
    @FXML
    private TableColumn colEmpresa;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField tfBuscarUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        usuarios = FXCollections.observableArrayList();
        configurarTabla();
        
    }
    
    public void inicializarInformacion(Integer idUsuario){
        this.idUsuario = idUsuario;
        
        descargarUsuarios();
        inicializarBusquedaUsuario();
    }
    
    private void configurarTabla(){
        colNombreUsuario.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApellidoP.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApellidoM.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colCurp.setCellValueFactory(new PropertyValueFactory("curp"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colUsername.setCellValueFactory(new PropertyValueFactory("nombreUsuario"));
        colRol.setCellValueFactory(new PropertyValueFactory("tipoAdmin"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory("nombreEmpresa"));
    }
    
    private void descargarUsuarios(){
        HashMap<String, Object> respuesta = UsuarioDAO.obtenerUsuariosIdRol(idUsuario);
        if(!(boolean) respuesta.get("error")){
            List<Usuario> listaWS = (List<Usuario>) respuesta.get("usuario");
            usuarios.addAll(listaWS);
            tvUsuario.setItems(usuarios);
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
    private void btnAgregarUsuario(ActionEvent event) {
        irFormularioUsuario(null, idUsuario);
        actualizarTabla();
    }
    
    private void irFormularioUsuario(Usuario usuario, Integer idUsuario){
         try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLFormularioUsuario.fxml"));
            Parent vista = loadVista.load();
            
            FXMLFormularioUsuarioController controller = loadVista.getController();
            controller.inicializarInformacion(usuario, idUsuario);
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Agregar usuario");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnEditarUsuario(ActionEvent event) {
        int posicionSeleccionada = tvUsuario.getSelectionModel().getSelectedIndex();
        if(posicionSeleccionada != -1){
            Usuario usuario = usuarios.get(posicionSeleccionada);
            irFormularioUsuario(usuario, idUsuario);
            actualizarTabla();
        }else{
            Utilidades.mostrarAlertaSimple("Selección de usuario", "Para poder modificar debes seleccionar un usuario de la tabla", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Usuario usuarioSeleccionado = tvUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null){
            Optional<ButtonType> respuesta = Utilidades.mostrarAlertaConfirmacion("Confirmar aliminación", 
                    "¿Estás seguro de eliminar al usuario " + usuarioSeleccionado.getNombre() + ", de su registro?");
            if(respuesta.get() == ButtonType.OK){
                eliminarPaciente(usuarioSeleccionado.getIdUsuario());
                tvUsuario.getItems().remove(usuarioSeleccionado);
                
                
                tvUsuario.setItems(null);
                tvUsuario.layout();
                tvUsuario.setItems(usuarios);
            }
        }   else{
            Utilidades.mostrarAlertaSimple("Selección de usuario", "Por favor, seleccione un usuario para eliminar.", Alert.AlertType.WARNING);
        }
    }
    
    private void eliminarPaciente(Integer idUsuario){
        Mensaje mensaje = UsuarioDAO.eliminarUsuario(idUsuario);
    }
    
    private void actualizarTabla(){
        usuarios.clear();
        descargarUsuarios();
    }
    
    private void inicializarBusquedaUsuario(){
        if(usuarios != null){
            FilteredList<Usuario> filtroUsuario = new FilteredList<>(usuarios, p -> true);
            tfBuscarUsuario.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    filtroUsuario.setPredicate(usuarioBusqueda -> {
                        //Regla cuando es vacío
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String lowerFilter = newValue.toLowerCase();
                        //Reglas de filtrado
                        if(usuarioBusqueda.getNombre().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }
                        if(usuarioBusqueda.getNombreUsuario().toLowerCase().contains(lowerFilter)){//Si lo quiseramos buscar por algo específico pondríamos equal
                            return true;
                        }//Me falta buscar por rol, tengo que hacer el INNER JOIN
                        return false;
                    });
                }
            });
            SortedList<Usuario> usuariosOrdenados = new SortedList<>(filtroUsuario);
            usuariosOrdenados.comparatorProperty().bind(tvUsuario.comparatorProperty());
            tvUsuario.setItems(usuariosOrdenados);
        }
    }
    
}
