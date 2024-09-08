package clienteescritoriocupones;

import clienteescritoriocupones.modelo.dao.CatalogoDAO;
import clienteescritoriocupones.modelo.dao.EmpresaDAO;
import clienteescritoriocupones.modelo.dao.PromocionDAO;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class FXMLFormularioPromocionController implements Initializable {

    private Promocion promocion;
    private int idPromocion;
    private int idUsuario;
    private boolean promocionlEdit = true;
    private File imagenSeleccionada;
    
    @FXML
    private ComboBox<Estatus> cbEstatus;
    @FXML
    private TextField tfNombrePromo;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfRestricciones;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<Empresa> cbEmpresa;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    @FXML
    private TextField tfCantidadRebajada;
    @FXML
    private ComboBox<Sucursal> cbSucursal;
    @FXML
    private ComboBox<TipoPromo> cbTipoPromo;
    @FXML
    private ComboBox<Categoria> cbCategoria;
    @FXML
    private TextField tfCodigoPromo;
    @FXML
    private Label lbCodigo;
    @FXML
    private ImageView ivImgPromocion;
    @FXML
    private Button btnActualizarF;
    @FXML
    private Button btnSeleccionarF;
    @FXML
    private Label lbNombrePromo;
    @FXML
    private Label lbDescripcion;
    @FXML
    private Label lbFechaInicio;
    @FXML
    private Label lbFechaFin;
    @FXML
    private Label lbRestricciones;
    @FXML
    private Label lbTipoPromo;
    @FXML
    private Label lbCantidadRebajada;
    @FXML
    private Label lbEstatus;
    @FXML
    private Label lbCategoria;
    @FXML
    private Label lbEmpresa;
    @FXML
    private Label lbSucursal;
    private ComboBox<Cupon> cbCupon;
    private Label lbCupon;
    @FXML
    private TextField tfCuponesDisponibles;
    @FXML
    private Label lbCuponesDisponibles;
    @FXML
    private Label lbCodigoPromo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarCombosBox();
        cbEmpresa.setOnAction(this::comboListenerEmpresa);
    }
    
    public void inicializarInformacion(Promocion promocion, Integer idPromocion){
        this.promocion = promocion;
        this.idPromocion = idPromocion;
        lbCodigo.setVisible(false);
        tfCodigoPromo.setVisible(false);
        btnActualizarF.setVisible(false);
        btnSeleccionarF.setVisible(false);
        ivImgPromocion.setVisible(false);
        //tfNumeroCupones.setDisable(true);
        //cbEmpresa.setDisable(true);
        
        if(promocion != null){
            lbCodigo.setVisible(true);
            tfCodigoPromo.setVisible(true);
            btnActualizarF.setVisible(true);
            btnSeleccionarF.setVisible(true);
            ivImgPromocion.setVisible(true);
            tfCuponesDisponibles.setDisable(true);
            tfNombrePromo.setText(promocion.getNombrePromo());
            tfDescripcion.setText(promocion.getDescripcion());
            tfRestricciones.setText(promocion.getRestricciones());
            
            
            String patter = "yyyy-MM-dd";
            DateTimeFormatter date = DateTimeFormatter.ofPattern(patter);
            
            dpFechaInicio.setValue(LocalDate.parse(promocion.getFechaInicioPromo(), date));
            dpFechaFin.setValue(LocalDate.parse(promocion.getFechaFinPromo(), date));
            
            tfCantidadRebajada.setText(Float.toString(promocion.getCantidadRebajada()));
            tfCuponesDisponibles.setText(Integer.toString(promocion.getCuponesDisponibles()));
            tfCodigoPromo.setText(promocion.getCodigoPromocion());
            asignarEstatus();
            asignarEmpresa();
            asignarTipoPromo();
            asignarCategoria();
            asignarSucursal();
            //asignarCupon();
            obtenerImagenServicio();
        }else{
            this.promocionlEdit = false;
        }
    }
    
    private void asignarEstatus(){
        int idEstatus = promocion.getIdEstatus();
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
            Utilidades.mostrarAlertaSimple("Error", "El estatus de la promoción no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }
    /*
    private void asignarCupon(){
        int idCupon = promocion.getIdCupon();
        // Buscar el objeto Estatus que coincide con el ID de la empresa
        Cupon cuponSeleccionado = null;
        for (Cupon cupon : cbCupon.getItems()) {
            if (cupon.getIdCupon() == idCupon) {
                cuponSeleccionado = cupon;
                break;
            }
        }
        // Establecer el Estatus encontrado como valor seleccionado en el ComboBox
        if (cuponSeleccionado != null) {
            cbCupon.setValue(cuponSeleccionado);
        } else {
            // Manejar el caso en el que el ID del estatus de la empresa no coincide con ningún Estatus en la lista
            // Puede ser útil mostrar un mensaje o manejarlo según tus necesidades
            Utilidades.mostrarAlertaSimple("Error", "El cupon de la promocion no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }*/
    
    private void asignarEmpresa(){
        int idEmpresa = promocion.getIdEmpresa();
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
    
    private void asignarTipoPromo(){
        int idTipoPromo = promocion.getIdTipoPromo();
        TipoPromo tipoPromoSeleccionada = null;
        for(TipoPromo tipoPromo : cbTipoPromo.getItems()){
            if(tipoPromo.getIdTipoPromo() == idTipoPromo){
                tipoPromoSeleccionada = tipoPromo;
                break;
            }
        }
        
        if(tipoPromoSeleccionada != null){
            cbTipoPromo.setValue(tipoPromoSeleccionada);
        }else{
            Utilidades.mostrarAlertaSimple("Error", "El tipo promoción no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }
    
    private void asignarCategoria(){
        int idCategoria = promocion.getIdCategoria();
        Categoria categoriaSeleccionada = null;
        for(Categoria categoria : cbCategoria.getItems()){
            if(categoria.getIdCategoria() == idCategoria){
                categoriaSeleccionada = categoria;
                break;
            }
        }
        
        if(categoriaSeleccionada != null){
            cbCategoria.setValue(categoriaSeleccionada);
        }else{
            Utilidades.mostrarAlertaSimple("Error", "La categoria no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }

    private void asignarSucursal(){
        int idSucursal = promocion.getIdSucursal();
        Sucursal sucursalSeleccionada = null;
        for(Sucursal sucursal : cbSucursal.getItems()){
            if(sucursal.getIdSucursal() == idSucursal){
                sucursalSeleccionada = sucursal;
                break;
            }
        }
        
        if(sucursalSeleccionada != null){
            cbSucursal.setValue(sucursalSeleccionada);
        }else{
            Utilidades.mostrarAlertaSimple("Error", "La sucursales no se encontró en la lista", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnGuardarPromocion(ActionEvent event) {
        String nombreP = tfNombrePromo.getText();
        String descripcion = tfDescripcion.getText();
        String restricciones = tfRestricciones.getText();
        String cantidadRebajada = tfCantidadRebajada.getText();
        String cuponesDisponibles = tfCuponesDisponibles.getText();
        String codigoPromo = tfCodigoPromo.getText();
        Estatus estatus = cbEstatus.getValue();
        Empresa empresa = cbEmpresa.getValue();
        TipoPromo tipoPromo = cbTipoPromo.getValue();
        Categoria categoria = cbCategoria.getValue();
        Sucursal sucursal = cbSucursal.getValue();
        //Cupon cupon = cbCupon.getValue();
        lbNombrePromo.setText("");
        lbDescripcion.setText("");
        lbFechaInicio.setText("");
        lbFechaFin.setText("");
        lbRestricciones.setText("");
        lbTipoPromo.setText("");
        lbCantidadRebajada.setText("");
        lbEstatus.setText("");
        lbCategoria.setText("");
        lbEmpresa.setText("");
        lbSucursal.setText("");
        lbCodigoPromo.setText("");
        lbCuponesDisponibles.setText("");
        boolean isValido = true;
        
        if(nombreP.isEmpty()){
            lbNombrePromo.setText("Campo obligatorio");
            isValido = false;
        }
        if(descripcion.isEmpty()){
            lbDescripcion.setText("Campo obligatorio");
            isValido = false;
        }
        if(restricciones.isEmpty()){
            lbRestricciones.setText("Campo obligatorio");
            isValido = false;
        }
        if(cantidadRebajada.isEmpty()){
            lbCantidadRebajada.setText("Campo obligatorio");
            isValido = false;
        }
        if(dpFechaInicio.getValue() == null){
            lbFechaInicio.setText("Campo obligatorio");
            isValido = false;
        }
        if(dpFechaFin.getValue() == null){
            lbFechaFin.setText("Campo obligatorio");
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
        if(cbTipoPromo.getSelectionModel().isEmpty()){
            lbTipoPromo.setText("Campo obligatorio");
            isValido = false;
        }
        if(cbCategoria.getSelectionModel().isEmpty()){
            lbCategoria.setText("Campo obligatorio");
            isValido = false;
        }
        if(cbSucursal.getSelectionModel().isEmpty()){
            lbSucursal.setText("Campo obligatorio");
            isValido = false;
        }
        if(cuponesDisponibles.isEmpty()){
            lbCuponesDisponibles.setText("Campos obligatorios");
            isValido = false;
        }
        if(isValido){
            if(promocionlEdit == true){
                Promocion promoEditada = new Promocion();
                promoEditada.setIdPromocion(this.promocion.getIdPromocion());
                promoEditada.setNombrePromo(nombreP);
                promoEditada.setDescripcion(descripcion);
                promoEditada.setRestricciones(restricciones);
                promoEditada.setCantidadRebajada(Float.parseFloat(cantidadRebajada));
                //promoEditada.setCuponesDisponibles(Integer.parseInt(cuponesDisponibles));
                promoEditada.setCodigoPromocion(codigoPromo);
                promoEditada.setIdEstatus(estatus.getIdEstatus());
                promoEditada.setIdEmpresa(empresa.getIdEmpresa());
                promoEditada.setIdTipoPromo(tipoPromo.getIdTipoPromo());
                promoEditada.setIdCategoria(categoria.getIdCategoria());
                promoEditada.setIdSucursal(sucursal.getIdSucursal());
                //promoEditada.setIdCupon(cupon.getIdCupon());
                promoEditada.setFechaInicioPromo(dpFechaInicio.getValue().toString());
                promoEditada.setFechaFinPromo(dpFechaFin.getValue().toString());
                editarPromocion(promoEditada);
            }else{
                Promocion promoNueva = new Promocion();
                promoNueva.setNombrePromo(nombreP);
                promoNueva.setDescripcion(descripcion);
                promoNueva.setRestricciones(restricciones);
                promoNueva.setCantidadRebajada(Float.parseFloat(cantidadRebajada));
                //promoNueva.setNumeroCupones(Integer.parseInt(numeroCupones));
                promoNueva.setCuponesDisponibles(Integer.parseInt(cuponesDisponibles));
                promoNueva.setCodigoPromocion(codigoPromo);
                promoNueva.setIdEstatus(estatus.getIdEstatus());
                promoNueva.setIdEmpresa(empresa.getIdEmpresa());
                promoNueva.setIdTipoPromo(tipoPromo.getIdTipoPromo());
                promoNueva.setIdCategoria(categoria.getIdCategoria());
                promoNueva.setIdSucursal(sucursal.getIdSucursal());
                //promoNueva.setIdCupon(cupon.getIdCupon());
                promoNueva.setFechaInicioPromo(dpFechaInicio.getValue().toString());
                promoNueva.setFechaFinPromo(dpFechaFin.getValue().toString());
                guardarPromocion(promoNueva);
            }
        }
    }
    
    private void guardarPromocion(Promocion promocion){
        Mensaje msj = PromocionDAO.registrarPromocion(promocion);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Promoción registrada", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al registrar", msj.getMensaje(), Alert.AlertType.ERROR
            );
        }
    }
    
    private void editarPromocion(Promocion promocion){
        Mensaje msj = PromocionDAO.editarPromocion(promocion);
        if(!msj.isError()){
            Utilidades.mostrarAlertaSimple("Promoción editada", msj.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        }else{
            Utilidades.mostrarAlertaSimple("Error al editar", msj.getMensaje(), Alert.AlertType.ERROR
            );
        }
    }

    private void cargarCombosBox(){
        
        List<Estatus> estatus = UsuarioDAO.obtenerEstatus();
        cbEstatus.getItems().clear();
        cbEstatus.getItems().addAll(estatus);
        
        List<Empresa> empresas = EmpresaDAO.obtenerEmpresa();
        cbEmpresa.getItems().clear();
        cbEmpresa.getItems().addAll(empresas);
        
        List<TipoPromo> tipoPromo = CatalogoDAO.obtenerTipoPromo();
        cbTipoPromo.getItems().clear();
        cbTipoPromo.getItems().addAll(tipoPromo);
        
        List<Categoria> categoria = CatalogoDAO.obtenerCategoria();
        cbCategoria.getItems().clear();
        cbCategoria.getItems().addAll(categoria);
        
        List<Sucursal> sucursal = CatalogoDAO.obtenerSucursal();
        cbSucursal.getItems().clear();
        cbSucursal.getItems().addAll(sucursal);
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) btnCancelar.getScene().getWindow();        
        escenario.close();
    }
    
    private void comboListenerEmpresa(ActionEvent event){
        Empresa empresaSeleccionada = cbEmpresa.getValue();
        if(empresaSeleccionada != null){
            //obtener la lista de los municipios de ese estado:
            List<Sucursal> sucursales = SucursalDAO.obtenerSucursalEmpresa(empresaSeleccionada.getIdEmpresa());
            //Limpiar los datos del combo por si las moscas
            cbSucursal.getItems().clear();
            //Agregar los municipios al comboBox
            cbSucursal.getItems().addAll(sucursales);
        }
    }
    
    @FXML
    private void clicSeleccionarFoto(ActionEvent event) {
        FileChooser dialogoSeleccionImg = new FileChooser();
        dialogoSeleccionImg.setTitle("Selecciona una imagen");
        //Configuración del tipo de archivo
        FileChooser.ExtensionFilter filtroImg = new FileChooser.ExtensionFilter("Archivos de iamgen (*.png, *.jpg)", "*.png", "*.jpg");
        dialogoSeleccionImg.getExtensionFilters().add(filtroImg);
        Stage stageActual = (Stage) tfNombrePromo.getScene().getWindow();
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
            Mensaje msj = PromocionDAO.subirFotoPromocion(promocion.getIdPromocion(), imgBytes);
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
            ivImgPromocion.setImage(image);
        }catch (IOException e){
            e.printStackTrace();
            
        }
    }
    
    private void obtenerImagenServicio(){
        Promocion promocionFoto = PromocionDAO.obtenerFotoPromocion(promocion.getIdPromocion());
        if(promocionFoto != null && promocionFoto.getFotoBase64() != null && !promocionFoto.getFotoBase64().isEmpty()){
            mostrarFotoServidor(promocionFoto.getFotoBase64());
        }
    }
    
    private void mostrarFotoServidor(String fotoBase64){
        byte[] foto = Base64.getDecoder().decode(fotoBase64.replaceAll("\\n", ""));
        Image img = new Image(new ByteArrayInputStream(foto));
        ivImgPromocion.setImage(img);
    }
}
