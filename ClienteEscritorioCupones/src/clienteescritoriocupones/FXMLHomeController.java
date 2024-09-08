package clienteescritoriocupones;

import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.RespuestaLogin;
import clienteescritoriocupones.modelo.pojo.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLHomeController implements Initializable {
    
    private Usuario usuarioSesion;
    private Empresa empresa;
    
    @FXML
    private Label lbNombreU;
    @FXML
    private AnchorPane lbTitulo;
    @FXML
    private Button btnEmpresa;
    @FXML
    private Button btnSucursal;
    @FXML
    private Button btnPromocion;
    @FXML
    private Button btnUsuario;
    @FXML
    private Label lbNombreEmpresa;
    @FXML
    private Button btnCupones;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void inicializarInformacionUsuario(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        lbNombreU.setText(usuarioSesion.getNombre() + " " + 
                usuarioSesion.getApellidoPaterno() + " " + usuarioSesion.getApellidoMaterno());
        lbNombreEmpresa.setText(usuarioSesion.getNombreEmpresa());
        //if(usuarioSesion.getIdRol() == 2){
            //btnEmpresa.setDisable(true);
            //btnUsuario.setDisable(true);
        //}
    }

    @FXML
    private void btnEmpresa(ActionEvent event) {
        try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLAdminEmpresa.fxml"));
            Parent vista = loadVista.load();
            
            FXMLAdminEmpresaController controller = loadVista.getController();
            controller.inicializarInformacion(usuarioSesion.getIdUsuario());
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Empresas");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnHome(ActionEvent event) {
    }

    @FXML
    private void btnSucursal(ActionEvent event) {
        try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLAdminSucursal.fxml"));
            Parent vista = loadVista.load();
            
            FXMLAdminSucursalController controller = loadVista.getController();
            controller.inicializarInformacion(usuarioSesion.getIdUsuario());
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Sucursales");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnPromocion(ActionEvent event) {
        try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLAdminPromocion.fxml"));
            Parent vista = loadVista.load();
            
            FXMLAdminPromocionController controller = loadVista.getController();
            controller.inicializarInformacion(usuarioSesion.getIdUsuario());
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Promoción");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnUsuario(ActionEvent event) {
        try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLAdminUsuario.fxml"));
            Parent vista = loadVista.load();
            
            FXMLAdminUsuarioController controller = loadVista.getController();
            controller.inicializarInformacion(usuarioSesion.getIdPromocion());
            System.out.println(usuarioSesion.getIdPromocion());
            
            Scene escenaAdmin = new Scene(vista);
            Stage escenarioAdmin = new Stage();
            escenarioAdmin.setScene(escenaAdmin);
            escenarioAdmin.setTitle("Usuarios");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCupones(ActionEvent event) {
        try{
            FXMLLoader loadVista = new FXMLLoader(getClass().getResource("FXMLAdminCupon.fxml"));
            Parent vista = loadVista.load();
            
            FXMLAdminCuponController controller = loadVista.getController();
            controller.inicializarInformacion(usuarioSesion.getIdPromocion());
            
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
}
