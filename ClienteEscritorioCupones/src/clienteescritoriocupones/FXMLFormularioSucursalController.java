package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.SucursalDAO;
import clienteescritoriocupones.modelo.dao.UsuarioDAO;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Estatus;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Sucursal;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLFormularioSucursalController implements Initializable {

    private Sucursal sucursal;
    private int idSucursal;
    private boolean sucursalEdit = true;
    
    @FXML
    private ComboBox<Estatus> cbEstatus;
    @FXML
    private TextField tfNombreS;
    @FXML
    private TextField tfNombreEncargado;
    @FXML
    private TextField tfTelefono;
    @FXML
    private ComboBox<Empresa> cbEmpresa;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lbNombreS;
    @FXML
    private Label lbNombreEncargado;
    @FXML
    private Label lbTelefono;
    @FXML
    private Label lbEmpresa;
    @FXML
    private Label lbEstatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarEstatus();
        cargarEmpresa();
    }    

    public void inicializarInformacion(Sucursal sucursal, Integer idSucursal){
        this.sucursal = sucursal;
        this.idSucursal = idSucursal;
        
        if(sucursal != null){
            tfNombreS.setText(sucursal.getNombreSucursal());
            tfNombreEncargado.setText(sucursal.getNombreEncargado());
            tfTelefono.setText(sucursal.getTelefono());
            asignarEstatus();
            asignarEmpresa();
        }else{
            this.sucursalEdit = false;
        }
    }
    
    private void asignarEstatus(){
        int idEstatus = sucursal.getIdEstatus();
        // Buscar el objeto Estatus que coincide con el ID de la empresa
        Estatus estatusSeleccionado = null;
        for (Estatus estatus : cbEstatus.getItems()) {
            if (estatus.getIdEstatus() == idEstatus) {
                estatusSeleccionado = estatus;
                break;
            }
        }
        // Establecer el Estatus encontrado como valor seleccionado en el ComboBox
        if (estatusSeleccionado != null) {
            cbEstatus.setValue(estatusSeleccionado);
        } else {
            // Manejar el caso en el que el ID del estatus de la empresa no coincide con ningún Estatus en la lista
            // Puede ser útil mostrar un mensaje o manejarlo según tus necesidades
            Utilidades.mostrarAlertaSimple("Error", "El estatus de la empresa no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }
    
    private void asignarEmpresa(){
        int idEmpresa = sucursal.getIdEmpresa();
        // Buscar el objeto Empresa que coincide con el ID de la empresa
        Empresa empresaSeleccionada = null;
        for (Empresa empresa : cbEmpresa.getItems()) {
            if (empresa.getIdEmpresa() == idEmpresa) {
                empresaSeleccionada = empresa;
                break;
            }
        }
        // Establecer el Estatus encontrado como valor seleccionado en el ComboBox
        if (empresaSeleccionada != null) {
            cbEmpresa.setValue(empresaSeleccionada);
        } else {
            // Manejar el caso en el que el ID del estatus de la empresa no coincide con ningún Estatus en la lista
            // Puede ser útil mostrar un mensaje o manejarlo según tus necesidades
            Utilidades.mostrarAlertaSimple("Error", "La empresa no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnGuardarSucursal(ActionEvent event) {
        String nombreS = tfNombreS.getText();
        String nombreE =tfNombreEncargado.getText();
        String telefono = tfTelefono.getText();
        Estatus estatus = cbEstatus.getValue();
        Empresa empresa = cbEmpresa.getValue();
        lbNombreS.setText("");
        lbNombreEncargado.setText("");
        lbTelefono.setText("");
        lbEmpresa.setText("");
        lbEstatus.setText("");
        boolean isValido = true;
        if(nombreS.isEmpty()){
            lbNombreS.setText("Campo obligatorio");
            isValido = false;
        }
        if(nombreE.isEmpty()){
            lbNombreEncargado.setText("Campo obligatorio");
            isValido = false;
        }
        if(telefono.isEmpty()){
            lbTelefono.setText("Campo obligatorio");
            isValido = false;
        }
        if(cbEstatus.getSelectionModel().isEmpty()){
            lbEstatus.setText("Campo obligatorio");
            isValido = false;
        }
        if(cbEmpresa.getSelectionModel().isEmpty()){
            lbEmpresa.setText("Campo obligatorio");
            isValido = false;
        }
        if(isValido){
            if(sucursalEdit == true){
                Sucursal sucursalEditada = new Sucursal();
                sucursalEditada.setIdSucursal(this.sucursal.getIdSucursal());
                sucursalEditada.setNombreSucursal(nombreS);
                sucursalEditada.setNombreEncargado(nombreE);
                sucursalEditada.setTelefono(telefono);
                sucursalEditada.setIdEstatus(estatus.getIdEstatus());
                sucursalEditada.setIdEmpresa(empresa.getIdEmpresa());
                editarSucursal(sucursalEditada);
            }else{
                Sucursal sucursalNueva = new Sucursal();
                sucursalNueva.setNombreSucursal(nombreS);
                sucursalNueva.setNombreEncargado(nombreE);
                sucursalNueva.setTelefono(telefono);
                sucursalNueva.setIdEstatus(estatus.getIdEstatus());
                sucursalNueva.setIdEmpresa(empresa.getIdEmpresa());
                guardarSucursal(sucursalNueva);
            }
        }
    }
    
    private void guardarSucursal(Sucursal sucursal){
        Mensaje msj = SucursalDAO.registrarSucursal(sucursal);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Sucursal registrada", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al registrar", msj.getMensaje(), Alert.AlertType.ERROR
            );
        }
    }
    
    private void editarSucursal(Sucursal sucursal){
        Mensaje msj = SucursalDAO.editarSucursal(sucursal);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Sucursal editada", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al editar", msj.getMensaje(), Alert.AlertType.ERROR
            );
        }
    }
    
    private void cargarEstatus(){
        List<Estatus> estatus = UsuarioDAO.obtenerEstatus();
        cbEstatus.getItems().clear();
        cbEstatus.getItems().addAll(estatus);
    }
    
    private void cargarEmpresa(){
        List<Empresa> empresas = EmpresaDAO.obtenerEmpresa();
        cbEmpresa.getItems().clear();
        cbEmpresa.getItems().addAll(empresas);
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
