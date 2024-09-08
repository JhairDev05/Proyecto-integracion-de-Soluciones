package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.CatalogoDAO;
import clienteescritoriocupones.modelo.dao.CuponDAO;
import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.SucursalDAO;
import clienteescritoriocupones.modelo.dao.UsuarioDAO;
import clienteescritoriocupones.modelo.pojo.Categoria;
import clienteescritoriocupones.modelo.pojo.Cupon;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Estatus;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Promocion;
import clienteescritoriocupones.modelo.pojo.Sucursal;
import clienteescritoriocupones.modelo.pojo.TipoPromo;
import clienteescritoriocupones.utils.Utilidades;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLCanjearCuponController implements Initializable {
    
    private Cupon cupon;
    private Promocion promocion;
    private int idPromocion;
    
    private ComboBox<Sucursal> cbSucursal;
    private ComboBox<Empresa> cbEmpresa;
    @FXML
    private Button btnCancelar;
    private ComboBox<Promocion> cbPromocion;
    @FXML
    private TextField tfCanjearCupon;
    @FXML
    private Label lbCanjearCupon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void inicializarInformacion(Cupon cupon, Integer idPromocion){
        this.cupon = cupon;
        this.idPromocion = idPromocion;
    }
    
    @FXML
    private void btnCanjearCupon(ActionEvent event) {
        String codigoPromo = tfCanjearCupon.getText();
        lbCanjearCupon.setText("");
        boolean isValido = true;
        
        if(codigoPromo.isEmpty()){
            lbCanjearCupon.setText("Campo obligatorio");
            isValido = false;
        }
        if(isValido){
            Cupon canjearCupon = new Cupon();
            canjearCupon.setIdPromocion(this.cupon.getIdPromocion());
            canjearCupon.setCodigoPromocion(codigoPromo);
            canjearCupon(canjearCupon);
        }
    }
    
    private void canjearCupon(Cupon cupon){
        Mensaje msj = CuponDAO.canjearCupon(cupon);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Cupón canjeado", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al canjear el cupón", msj.getMensaje(), Alert.AlertType.ERROR
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
