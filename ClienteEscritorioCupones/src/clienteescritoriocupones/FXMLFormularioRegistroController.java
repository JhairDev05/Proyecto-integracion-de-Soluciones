package clienteescritoriocupones;

import clienteescritoriocupones.interfas.IRespuesta;
import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.UsuarioDAO;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Rol;
import clienteescritoriocupones.modelo.pojo.Usuario;
import clienteescritoriocupones.utils.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class FXMLFormularioRegistroController implements Initializable {
    
    private IRespuesta observador;

    @FXML
    private Button btnCancelar;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoP;
    @FXML
    private TextField tfApellidoM;
    @FXML
    private TextField tfNombreUsuario;
    @FXML
    private TextField tfCurp;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField pfPassword;
    @FXML
    private ComboBox<Rol> cbRol;
    @FXML
    private ComboBox<Empresa> cbEmpresa;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbApellidoP;
    @FXML
    private Label lbApellidoM;
    @FXML
    private Label lbNombreUsuario;
    @FXML
    private Label lbCurp;
    @FXML
    private Label lbCorreo;
    @FXML
    private Label lbPassword;
    @FXML
    private Label lbRol;
    @FXML
    private Label lbEmpresa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarRol();
        cargarEmpresa();
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        //Stage escenario = (Stage) btnCancelar.getScene().getWindow();        
        //escenario.close(); 
        Stage stageActual = (Stage) btnCancelar.getScene().getWindow();
        try {
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource("FXMLInicioSesion.fxml"));
            Parent vista = loadMain.load();
            
            
            Scene escena = new Scene(vista);
            stageActual.setScene(escena);
            stageActual.show();
            stageActual.setTitle("Iniciar sesión");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnAgregarUsuario(ActionEvent event) {
        String nombre = tfNombre.getText();
        String apellidoP = tfApellidoP.getText();
        String apellidoM = tfApellidoM.getText();
        String nombreU = tfNombreUsuario.getText();
        String curp = tfCurp.getText();
        String correo = tfCorreo.getText();
        String password = pfPassword.getText();
        Rol rolSeleccionado = cbRol.getValue();
        Empresa empresaSeleccionada = cbEmpresa.getValue();
        lbNombre.setText("");
        lbApellidoP.setText("");
        lbApellidoM.setText("");
        lbNombreUsuario.setText("");
        lbCurp.setText("");
        lbCorreo.setText("");
        lbPassword.setText("");
        lbRol.setText("");
        lbEmpresa.setText("");
        boolean isValido = true;
        if(nombre.isEmpty()){
            lbNombre.setText("Campo obligatorio");
            isValido = false;
        }
        if(apellidoP.isEmpty()){
            lbApellidoP.setText("Campo obligatorio");
            isValido = false;
        }
        if(apellidoM.isEmpty()){
            lbApellidoM.setText("Campo obligatorio");
            isValido = false;
        }
        if(nombreU.isEmpty()){
            lbNombreUsuario.setText("Campo obligatorio");
            isValido = false;
        }
        if(curp.isEmpty()){
            lbCurp.setText("Campo obligatorio");
            isValido = false;
        }
        if(correo.isEmpty()){
            lbCorreo.setText("Campo obligatorio");
            isValido = false;
        }
        if(password.isEmpty()){
            lbPassword.setText("Campo obligatorio");
            isValido = false;
        }
        if(cbRol.getSelectionModel().isEmpty()){
            lbRol.setText("Campo obligatorio");
            isValido = false;
        }
        if(cbEmpresa.getSelectionModel().isEmpty()){
            lbEmpresa.setText("Campo obligatorio");
            isValido = false;
        }
        if(isValido){
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setNombre(nombre);
            usuarioNuevo.setApellidoPaterno(apellidoP);
            usuarioNuevo.setApellidoMaterno(apellidoM);
            usuarioNuevo.setNombreUsuario(nombreU);
            usuarioNuevo.setCurp(curp);
            usuarioNuevo.setCorreo(correo);
            usuarioNuevo.setPassword(password);

            // Asignar el rol y la empresa al usuario
            usuarioNuevo.setIdRol(rolSeleccionado.getIdRol()); // Asignar el rol seleccionado al usuario
            usuarioNuevo.setIdEmpresa(empresaSeleccionada.getIdEmpresa()); // Asignar la empresa seleccionada al usuario
            guardarUsuario(usuarioNuevo); 
        }
    }
    
    private void guardarUsuario(Usuario usuario){
        Mensaje msj = UsuarioDAO.registrarUsuario(usuario);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Usuario registrado", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al registrar", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
    private void cargarRol(){
        List<Rol> roles = UsuarioDAO.obtenerRol();
        cbRol.getItems().clear();
        cbRol.getItems().addAll(roles);
    }
    
    private void cargarEmpresa(){
        List<Empresa> empresas = EmpresaDAO.obtenerEmpresa();
        cbEmpresa.getItems().clear();
        cbEmpresa.getItems().addAll(empresas);
    }
    
    private void cerrarVentana(){
        Stage stageActual = (Stage) tfNombre.getScene().getWindow();
        try {
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource("FXMLInicioSesion.fxml"));
            Parent vista = loadMain.load();

            //Parent vista = FXMLLoader.load(getClass().getResource("FXMLHome.fxml"));
            Scene escena = new Scene(vista);
            stageActual.setScene(escena);
            stageActual.show();
            stageActual.setTitle("Iniciar sesión");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
