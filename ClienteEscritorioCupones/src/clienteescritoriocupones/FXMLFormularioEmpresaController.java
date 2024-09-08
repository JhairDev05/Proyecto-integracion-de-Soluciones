package clienteescritoriocupones;

import clienteescritoriocupones.interfas.IRespuesta;
import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.UsuarioDAO;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Estatus;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Usuario;
import clienteescritoriocupones.utils.Utilidades;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class FXMLFormularioEmpresaController implements Initializable {

    private Empresa empresa;
    private int idEmpresa;
    private IRespuesta observador;
    private boolean empresaEdit = true;
    private File imagenSeleccionada;
    
    @FXML
    private ComboBox<Estatus> cbEstatus;
    @FXML
    private ImageView ivLogoEmpresa;
    @FXML
    private TextField tfNombreEmpresa;
    @FXML
    private TextField tfNombreComercial;
    @FXML
    private TextField tfNombreR;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfPaginaWeb;
    @FXML
    private TextField tfRfc;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnSeleccionarF;
    @FXML
    private Button btnActualizar;
    @FXML
    private Label lbTelefono;
    @FXML
    private Label lbPaginaWeb;
    @FXML
    private Label lbCorreo;
    @FXML
    private Label lbEstatus;
    @FXML
    private Label lbNombreR;
    @FXML
    private Label lbNombreC;
    @FXML
    private Label lbNombreE;
    @FXML
    private Label lbRfc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarEstatus();
    }    
    
    public void inicializarEmpresa(Empresa empresa, Integer idEmpresa){
        this.empresa = empresa;
        this.idEmpresa = idEmpresa;
        ivLogoEmpresa.setVisible(false);
        btnSeleccionarF.setVisible(false);
        btnActualizar.setVisible(false);
        //permisosUusario();
        cbEstatus.setVisible(false);
        
        if(empresa != null){
            ivLogoEmpresa.setVisible(true);
            btnSeleccionarF.setVisible(true);
            btnActualizar.setVisible(true);
            cbEstatus.setVisible(true);
            tfRfc.setDisable(true);
            tfNombreEmpresa.setText(empresa.getNombreEmpresa());
            tfNombreComercial.setText(empresa.getNombreComercial());
            tfNombreR.setText(empresa.getNombreRepresentante());
            tfTelefono.setText(empresa.getTelefono());
            tfCorreo.setText(empresa.getEmail());
            tfPaginaWeb.setText(empresa.getPaginaWeb());
            tfRfc.setText(empresa.getRfc());
            asignarEstatus();
            obtenerImagenServicio();
        }else{
            this.empresaEdit = false;
        }
    }
    
    private void permisosUusario(){
        Usuario usuario = new Usuario();
        if(usuario.getIdRol() == 1){
            cbEstatus.setVisible(true);
        }else if(usuario.getIdRol() == 2){
            cbEstatus.setVisible(false);
        }
    }
    
    private void asignarEstatus(){
        int idEstatus = empresa.getIdEstatus();

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

    @FXML
    private void btnGuardar(ActionEvent event) {
        String nombreE = tfNombreEmpresa.getText();
        String nombreC = tfNombreComercial.getText();
        String nombreR = tfNombreR.getText();
        String telefono = tfTelefono.getText();
        String correo = tfCorreo.getText();
        String paginaWeb = tfPaginaWeb.getText();
        String rfc = tfRfc.getText();
        Estatus estatus = cbEstatus.getValue();
        lbTelefono.setText("");
        lbPaginaWeb.setText("");
        lbCorreo.setText("");
        //lbEstatus.setText("");
        lbNombreR.setText("");
        lbNombreC.setText("");
        lbNombreE.setText("");
        lbRfc.setText("");
        boolean isValido = true;
        if(nombreE.isEmpty()){
            lbNombreE.setText("Campo obligatorio");
            isValido = false;
        }
        if(nombreC.isEmpty()){
            lbNombreC.setText("Campo obligatorio");
            isValido = false;
        }
        if(nombreR.isEmpty()){
            lbNombreR.setText("Campo obligatorio");
            isValido = false;
        }
        if(telefono.isEmpty()){
            lbTelefono.setText("Campo obligatorio");
            isValido = false;
        }
        if(correo.isEmpty()){
            lbCorreo.setText("Campo obligatorio");
            isValido = false;
        }
        if(paginaWeb.isEmpty()){
            lbPaginaWeb.setText("Campo obligatorio");
            isValido = false;
        }
        if(rfc.isEmpty()){
            lbRfc.setText("Campo obligatorio");
            isValido = false;
        }
        if(isValido){
            if(empresaEdit == true){
                Empresa empresaEditada = new Empresa();
                empresaEditada.setIdEmpresa(this.empresa.getIdEmpresa());
                empresaEditada.setNombreEmpresa(nombreE);
                empresaEditada.setNombreComercial(nombreC);
                empresaEditada.setNombreRepresentante(nombreR);
                empresaEditada.setTelefono(telefono);
                empresaEditada.setEmail(correo);
                empresaEditada.setPaginaWeb(paginaWeb);
                empresaEditada.setRfc(rfc);
                empresaEditada.setIdEstatus(estatus.getIdEstatus());
                editarEmpresa(empresaEditada);
            }else{
                Empresa empresaNueva = new Empresa();
                empresaNueva.setNombreEmpresa(nombreE);
                empresaNueva.setNombreComercial(nombreC);
                empresaNueva.setNombreRepresentante(nombreR);
                empresaNueva.setTelefono(telefono);
                empresaNueva.setEmail(correo);
                empresaNueva.setPaginaWeb(paginaWeb);
                empresaNueva.setRfc(rfc);
                guardarEmpresa(empresaNueva);
            }
        }
    }
    
    private void guardarEmpresa(Empresa empresa){
        Mensaje msj = EmpresaDAO.registrarEmpresa(empresa);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Empresa registrada", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al registrar", msj.getMensaje(), Alert.AlertType.ERROR
            );
        }
    }
    
    private void editarEmpresa(Empresa empresa){
        Mensaje msj = EmpresaDAO.editarEmpresa(empresa);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Empresa editada", msj.getMensaje(), Alert.AlertType.INFORMATION);
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

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) btnCancelar.getScene().getWindow();        
        escenario.close();
    }
    
    @FXML
    private void clicSeleccionarFoto(ActionEvent event) {
        FileChooser dialogoSeleccionImg = new FileChooser();
        dialogoSeleccionImg.setTitle("Selecciona una imagen");
        //Configuración del tipo de archivo
        FileChooser.ExtensionFilter filtroImg = new FileChooser.ExtensionFilter("Archivos de iamgen (*.png, *.jpg)", "*.png", "*.jpg");
        dialogoSeleccionImg.getExtensionFilters().add(filtroImg);
        Stage stageActual = (Stage) tfNombreEmpresa.getScene().getWindow();
        imagenSeleccionada = dialogoSeleccionImg.showOpenDialog(stageActual);
        if(imagenSeleccionada != null){
            mostrarFotoSeleccionada(imagenSeleccionada);
        }
    }
    
    @FXML
    private void clicActualizarFoto(ActionEvent event) {
        if(imagenSeleccionada != null){
            cargarFotoServidor(imagenSeleccionada);
        }else{
            Utilidades.mostrarAlertaSimple("Selecciona una fotografía", 
                    "Para subir una fotografía debes seleccionarla previamente", Alert.AlertType.WARNING);
        }
    }
    
    private void cargarFotoServidor(File imagen){
        try {
            byte[] imgBytes = Files.readAllBytes(imagen.toPath());
            Mensaje msj = EmpresaDAO.subirFotoEmpresa(empresa.getIdEmpresa(), imgBytes);
            if(!msj.isError()){
                Utilidades.mostrarAlertaSimple("Fotografía enviada", msj.getMensaje(), Alert.AlertType.INFORMATION);
            }else{
                Utilidades.mostrarAlertaSimple("Error", msj.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch (IOException ex) {
                Utilidades.mostrarAlertaSimple("Error", "Error: "+ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void mostrarFotoSeleccionada(File img){
        try{
            BufferedImage buffer = ImageIO.read(img);
            Image image = SwingFXUtils.toFXImage(buffer, null);
            ivLogoEmpresa.setImage(image);
        }catch (IOException e){
            e.printStackTrace();
            
        }
    }
    
    private void obtenerImagenServicio(){
        Empresa empresaFoto = EmpresaDAO.obtenerFotoEmpresa(empresa.getIdEmpresa());
        if(empresaFoto != null && empresaFoto.getFotoBase64() != null && !empresaFoto.getFotoBase64().isEmpty()){
            mostrarFotoServidor(empresaFoto.getFotoBase64());
        }
    }
    
    private void mostrarFotoServidor(String fotoBase64){
        byte[] foto = Base64.getDecoder().decode(fotoBase64.replaceAll("\\n", ""));
        Image img = new Image(new ByteArrayInputStream(foto));
        ivLogoEmpresa.setImage(img);
    }
}

