package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.CuponDAO;
import clienteescritoriocupones.modelo.dao.SucursalDAO;
import clienteescritoriocupones.modelo.pojo.Cupon;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Sucursal;
import clienteescritoriocupones.utils.Utilidades;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLFormularioCuponController implements Initializable {

    private Cupon cupon;
    private Integer idCupon;
    private boolean cuponEdit = true;
    
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField tfCuponesDisponibles;
    @FXML
    private Label lbCuponesD;
    @FXML
    private Label lbCuponesDisponibles;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Cupon cupon, Integer idCupon){
        this.cupon = cupon;
        this.idCupon = idCupon;
        
        if(cupon != null){
            tfCuponesDisponibles.setText(Integer.toString(cupon.getCuponesDisponibles()));
        }else{
            this.cuponEdit = false;
        }
    }

    @FXML
    private void btnGuardarCupon(ActionEvent event) {
        String cuponesDisponibles = tfCuponesDisponibles.getText();
        lbCuponesDisponibles.setText("");
        boolean isValido = true;
        if(cuponesDisponibles.isEmpty()){
            lbCuponesDisponibles.setText("Campo obligatorio");
            isValido = false;
        }
        if(isValido){
            if(cuponEdit == true){
                Cupon cuponEditado = new Cupon();
                cuponEditado.setIdCupon(this.cupon.getIdCupon());
                cuponEditado.setCuponesDisponibles(Integer.parseInt(cuponesDisponibles));
                editarCupon(cuponEditado);
            }else{
                Cupon nuevoCupon = new Cupon();
                nuevoCupon.setCuponesDisponibles(Integer.parseInt(cuponesDisponibles));
                guardarCupon(nuevoCupon);
            }
        }
    }
    
    private void guardarCupon(Cupon cupon){
        Mensaje msj = CuponDAO.registrarCupon(cupon);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Cupón registrado", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al registrar", msj.getMensaje(), Alert.AlertType.ERROR
            );
        }
    }
    
    private void editarCupon(Cupon cupon){
        Mensaje msj = CuponDAO.editarCupon(cupon);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Cupón editado", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al editar", msj.getMensaje(), Alert.AlertType.ERROR
            );
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) btnCancelar.getScene().getWindow();        
        escenario.close();
    }
    
}
