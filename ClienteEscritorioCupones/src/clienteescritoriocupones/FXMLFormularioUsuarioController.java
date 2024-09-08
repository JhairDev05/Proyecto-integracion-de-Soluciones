package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.UsuarioDAO;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Rol;
import clienteescritoriocupones.modelo.pojo.Usuario;
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

public class FXMLFormularioUsuarioController implements Initializable {
    
    private boolean usuarioEdit = true;
    private Usuario usuario;
    private Integer idUsuario;

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
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoP;
    @FXML
    private TextField tfApellidoM;
    @FXML
    private ComboBox<Empresa> cbEmpresa;
    @FXML
    private TextField tfCurp;
    @FXML
    private TextField tfCorreo;
    @FXML
    private ComboBox<Rol> cbRol;
    @FXML
    private TextField pfPassword;
    @FXML
    private TextField tfNombreUsuario;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarRol();
        cargarEmpresa();
    }
    
    public void inicializarInformacion(Usuario usuario, Integer idUsuario){
        this.usuario = usuario;
        this.idUsuario = idUsuario;
        
        if(usuario != null){
            tfNombre.setText(usuario.getNombre());
            tfApellidoP.setText(usuario.getApellidoPaterno());
            tfApellidoM.setText(usuario.getApellidoMaterno());
            tfCurp.setText(usuario.getCurp());
            tfCorreo.setText(usuario.getCorreo());
            tfNombreUsuario.setText(usuario.getNombreUsuario());
            pfPassword.setText(usuario.getPassword());
            asignarRol();
            asignarEmpresa();
        }else{
            this.usuarioEdit = false;
        }
    }
    
    private void asignarRol(){
        int idRol = usuario.getIdRol();
        // Buscar el objeto Estatus que coincide con el ID de la empresa
        Rol rolSeleccionado = null;
        for (Rol estatus : cbRol.getItems()) {
            if (estatus.getIdRol() == idRol) {
                rolSeleccionado = estatus;
                break;
            }
        }
        // Establecer el Estatus encontrado como valor seleccionado en el ComboBox
        if (rolSeleccionado != null) {
            cbRol.setValue(rolSeleccionado);
        } else {
            // Manejar el caso en el que el ID del estatus de la empresa no coincide con ningún Estatus en la lista
            // Puede ser útil mostrar un mensaje o manejarlo según tus necesidades
            Utilidades.mostrarAlertaSimple("Error", "El rol del usuario no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }
    
    private void asignarEmpresa(){
        int idEmpresa = usuario.getIdEmpresa();
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
            if(usuarioEdit == true){
                Usuario usuarioEditado = new Usuario();
                usuarioEditado.setIdUsuario(this.usuario.getIdUsuario());
                usuarioEditado.setNombre(nombre);
                usuarioEditado.setApellidoPaterno(apellidoP);
                usuarioEditado.setApellidoMaterno(apellidoM);
                usuarioEditado.setNombreUsuario(nombreU);
                usuarioEditado.setCurp(curp);
                usuarioEditado.setCorreo(correo);
                usuarioEditado.setPassword(password);
                usuarioEditado.setIdEmpresa(empresaSeleccionada.getIdEmpresa());
                usuarioEditado.setIdRol(rolSeleccionado.getIdRol());
                editarUsuario(usuarioEditado);
            }else{
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
    
    private void editarUsuario(Usuario usuario){
        Mensaje msj = UsuarioDAO.editarUsuario(usuario);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("usuario editado", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al editar", msj.getMensaje(), Alert.AlertType.ERROR
            );
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
    
    
    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) btnCancelar.getScene().getWindow();        
        escenario.close();
    }
}
