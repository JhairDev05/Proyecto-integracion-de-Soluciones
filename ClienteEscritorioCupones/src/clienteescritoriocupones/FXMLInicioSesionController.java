package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.InicioSesionDAO;
import clienteescritoriocupones.modelo.pojo.RespuestaLogin;
import clienteescritoriocupones.modelo.pojo.Usuario;
import clienteescritoriocupones.utils.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class FXMLInicioSesionController implements Initializable {

    @FXML
    private AnchorPane loginForm;
    @FXML
    private AnchorPane registerForm;
    @FXML
    private TextField tfNombreUsuario;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnRegistro;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btnIniciarSesion(ActionEvent event){
        String nombreUsuario = tfNombreUsuario.getText();
        String password = pfPassword.getText();
        lbNombre.setText("");
        lbPassword.setText("");
        boolean isValido = true;
        if(nombreUsuario.isEmpty()){
            lbNombre.setText("El nombre de usuario es obligatorio");
            isValido = false;
        }
        if(password.isEmpty()){
            lbPassword.setText("La contraseña es obligatoria");
            isValido=false;
        }
        
        if(isValido){
            verificarCredenciales(nombreUsuario, password);
        }
    }
    
    public void verificarCredenciales(String nombreUsuario, String password){
        RespuestaLogin respuesta = InicioSesionDAO.iniciarSesion(nombreUsuario, password);
        if(respuesta.isError() == false){
            Utilidades.mostrarAlertaSimple("Credenciales Correctas", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            irPantallaHome(respuesta.getUsuarioSesion());
        }else{
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaHome(Usuario usuario){
        Stage stageActual = (Stage) lbNombre.getScene().getWindow();
        try {
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource("FXMLHome.fxml"));
            Parent vista = loadMain.load();
            
            FXMLHomeController controladorHome = loadMain.getController();
            controladorHome.inicializarInformacionUsuario(usuario);
            //Parent vista = FXMLLoader.load(getClass().getResource("FXMLHome.fxml"));
            Scene escena = new Scene(vista);
            stageActual.setScene(escena);
            stageActual.show();
            stageActual.setTitle("Home");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void btnIrFormularioRegistro(ActionEvent event) {
        Stage stageActual = (Stage) lbNombre.getScene().getWindow();
        try {
            FXMLLoader loadMain = new FXMLLoader(getClass().getResource("FXMLFormularioRegistro.fxml"));
            Parent vista = loadMain.load();
            
            
            Scene escena = new Scene(vista);
            stageActual.setScene(escena);
            stageActual.show();
            stageActual.setTitle("Registrar");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
