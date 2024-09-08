package clienteescritoriocupones.modelo.dao;

import clienteescritoriocupones.modelo.ConexionWS;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Promocion;
import clienteescritoriocupones.modelo.pojo.RespuestaHTTP;
import clienteescritoriocupones.modelo.pojo.Sucursal;
import clienteescritoriocupones.modelo.pojo.Usuario;
import clienteescritoriocupones.utils.Constantes;
import clienteescritoriocupones.utils.Utilidades;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javafx.scene.control.Alert;

public class SucursalDAO {
    
    public static Mensaje registrarSucursal(Sucursal sucursalNueva){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"sucursal/registrar";
        String parametros = String.format("nombreSucursal=%s&telefono=%s&nombreEncargado=%s&idEstatus=%s&idEmpresa=%s", 
                sucursalNueva.getNombreSucursal(), sucursalNueva.getTelefono(), sucursalNueva.getNombreEncargado(), 
                sucursalNueva.getIdEstatus(), sucursalNueva.getIdEmpresa());
        RespuestaHTTP respuesta = ConexionWS.peticionPOST(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al registrar sucursal, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static Mensaje editarSucursal(Sucursal sucursalEdicion){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"sucursal/editar";
        String parametros = String.format("idSucursal=%s&nombreSucursal=%s&telefono=%s&nombreEncargado=%s&idEstatus=%s&"
                + "idEmpresa=%s", 
                sucursalEdicion.getIdSucursal(), sucursalEdicion.getNombreSucursal(), sucursalEdicion.getTelefono(), 
                sucursalEdicion.getNombreEncargado(), sucursalEdicion.getIdEstatus(), sucursalEdicion.getIdEmpresa());
        RespuestaHTTP respuesta = ConexionWS.peticionPUT(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al editar sucursal, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static HashMap<String, Object> obtenerSucursalByEmpresa(Integer idEmpresa){
        HashMap<String, Object> respService = new LinkedHashMap();
        List<Sucursal> sucursal = null;
        String url = Constantes.URL_WS+"empresa/obtenerSucursalEmpresa/" + idEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            Type listaTipoSucursal = new TypeToken<List<Sucursal>>(){}.getType();
            sucursal = gson.fromJson(respuesta.getContenido(), listaTipoSucursal);
            respService.put("error", false);
            respService.put("sucursal", sucursal);
        }else{
           respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de las sucursales"); 
        }
        return respService;
    }
    
    public static HashMap<String, Object> obtenerEmpresaSucursalByUsuario(Integer idUsuario){
        HashMap<String, Object> respService = new LinkedHashMap();
        List<Sucursal> sucursal = null;
        String url = Constantes.URL_WS+"sucursal/obtenerSucursalRol/" + idUsuario;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            Type listaTipoSucursal = new TypeToken<List<Sucursal>>(){}.getType();
            sucursal = gson.fromJson(respuesta.getContenido(), listaTipoSucursal);
            respService.put("error", false);
            respService.put("sucursal", sucursal);
        }else{
            respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de las sucursales");
        }
        return respService;
    }
    
    public static Mensaje eliminarSucursal(Integer idSucursal){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"sucursal/eliminar";
        String parametros = "idSucursal=" + idSucursal;
        RespuestaHTTP respuesta = ConexionWS.peticionDELETE(url, parametros);
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);

            // Verificar si el mensaje indica que la eliminación se realizó correctamente
            if (!msj.isError()) {
                Utilidades.mostrarAlertaSimple("Sucursal eliminada", " Sucursal eliminada correctamente ", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlertaSimple("Error al eliminar la sucursal", msj.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error al eliminar la empresa", respuesta.getContenido(), Alert.AlertType.ERROR);
        }
        return msj;
    }
    
    /*
    public static HashMap<String, Object> obtenerSucursalEmpresaUsuario(Usuario usuarioSesion) {
        HashMap<String, Object> respService = new LinkedHashMap<>();
        List<Sucursal> sucursal = null;
        String url = "";
        
        if (usuarioSesion.getIdRol() == 1) {
            url = Constantes.URL_WS + "sucursal/obtenerSucursal";
        } else if (usuarioSesion.getIdRol() == 2) {
            url = Constantes.URL_WS + "empresa/obtenerSucursalEmpresa/" + usuarioSesion.getIdEmpresa();
        }

        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type listaTipoSucursal = new TypeToken<List<Sucursal>>(){}.getType();
            sucursal = gson.fromJson(respuesta.getContenido(), listaTipoSucursal);
            respService.put("error", false);
            respService.put("sucursal", sucursal);
        } else {
            respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de las sucursales");
        }
        return respService;
    }*/
    
    
    public static List<Sucursal> consumirServicioBusqueda(String nombreSucursal){
        List<Sucursal> sucursal = new ArrayList<>();
        String url = Constantes.URL_WS+"sucursal/buscarNombreSucursal/" + nombreSucursal;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaSucursal =  new TypeToken<List<Sucursal>>(){}.getType();
            Gson gson = new Gson();
            sucursal = gson.fromJson(respuesta.getContenido(), tipoListaSucursal);
        }
        return sucursal;
    }
    
    public static List<Sucursal> obtenerSucursalEmpresa(int idEmpresa){
        List<Sucursal> sucursales = new ArrayList<>();
        String url = Constantes.URL_WS + "empresa/obtenerSucursalEmpresa/" + idEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaSucursal = new TypeToken<List<Sucursal>>(){}.getType();
            Gson gson = new Gson();
            sucursales = gson.fromJson(respuesta.getContenido(), tipoListaSucursal);
        }
        return sucursales;
    }
    
    public static List<Promocion> obtenerPromoSucursal(int idSucursal){
        List<Promocion> promociones = new ArrayList<>();
        String url = Constantes.URL_WS + "sucursal/obtenerPromoSucursal/" + idSucursal;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaPromocion = new TypeToken<List<Promocion>>(){}.getType();
            Gson gson = new Gson();
            promociones = gson.fromJson(respuesta.getContenido(), tipoListaPromocion);
        }
        return promociones;
    }
}
