
package clienteescritoriocupones.modelo.dao;

import clienteescritoriocupones.modelo.ConexionWS;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Promocion;
import clienteescritoriocupones.modelo.pojo.RespuestaHTTP;
import clienteescritoriocupones.modelo.pojo.Sucursal;
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

public class PromocionDAO {
    
    public static Mensaje registrarPromocion(Promocion promocionNueva){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"promocion/registrar";
        String parametros = String.format("cuponesDisponibles=%s&nombrePromo=%s&descripcion=%s&fechaInicioPromo=%s&"
                + "fechaFinPromo=%s&restricciones=%s&cantidadRebajada=%s&idSucursal=%s&idTipoPromo=%s&idCategoria=%s&"
                + "idEmpresa=%s&idSucursales=%s", 
                promocionNueva.getCuponesDisponibles(), promocionNueva.getNombrePromo(), promocionNueva.getDescripcion(), 
                promocionNueva.getFechaInicioPromo(), promocionNueva.getFechaFinPromo(), promocionNueva.getRestricciones(),
                promocionNueva.getCantidadRebajada(), promocionNueva.getIdSucursal(), promocionNueva.getIdTipoPromo(),
                promocionNueva.getIdCategoria(), promocionNueva.getIdEmpresa(), 
                promocionNueva.getIdEstatus(), promocionNueva.getIdSucursal());
        RespuestaHTTP respuesta = ConexionWS.peticionPOST(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al registrar promoción, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static Mensaje editarPromocion(Promocion promocionEdicion){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"promocion/editar";
        String parametros = String.format("idPromocion=%s&nombrePromo=%s&descripcion=%s&fechaInicioPromo=%s&"
                + "fechaFinPromo=%s&restricciones=%s&cantidadRebajada=%s&codigoPromocion=%s&"
                + "idSucursal=%s&idTipoPromo=%s&idCategoria=%s&idEmpresa=%s&idEstatus=%s", 
                promocionEdicion.getIdPromocion(), promocionEdicion.getNombrePromo(), promocionEdicion.getDescripcion(), 
                promocionEdicion.getFechaInicioPromo(), promocionEdicion.getFechaFinPromo(), 
                promocionEdicion.getRestricciones(), promocionEdicion.getCantidadRebajada(), promocionEdicion.getCodigoPromocion(), 
                promocionEdicion.getIdSucursal(), promocionEdicion.getIdTipoPromo(), promocionEdicion.getIdCategoria(), 
                promocionEdicion.getIdEmpresa(), promocionEdicion.getIdEstatus());
        RespuestaHTTP respuesta = ConexionWS.peticionPUT(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al editar promoción, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static Mensaje eliminarPromocion(Integer idPromocion){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"promocion/eliminar";
        String parametros = "idPromocion=" + idPromocion;
        RespuestaHTTP respuesta = ConexionWS.peticionDELETE(url, parametros);
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);

            // Verificar si el mensaje indica que la eliminación se realizó correctamente
            if (!msj.isError()) {
                Utilidades.mostrarAlertaSimple("Promoción eliminada", " Promoción eliminada correctamente ", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlertaSimple("Error al eliminar la promoción", msj.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error al eliminar la promoción", respuesta.getContenido(), Alert.AlertType.ERROR);
        }
        return msj;
    }
    
    public static HashMap<String, Object> obtenerPromoEmpresaByUsuario(Integer idUsuario){
        HashMap<String, Object> respService = new LinkedHashMap();
        List<Promocion> promocion = null;
        String url = Constantes.URL_WS+"promocion/obtenerPromoSucursal/" + idUsuario;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            Type listaTipoSucursal = new TypeToken<List<Promocion>>(){}.getType();
            promocion = gson.fromJson(respuesta.getContenido(), listaTipoSucursal);
            respService.put("error", false);
            respService.put("promocion", promocion);
        }else{
            respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de las promociones");
        }
        return respService;
    }
    
    public static List<Promocion> consumirServicioBusqueda(String nombrePromo){
        List<Promocion> promocion = new ArrayList<>();
        String url = Constantes.URL_WS+"promocion/buscarPromocion/" + nombrePromo;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaPromo =  new TypeToken<List<Promocion>>(){}.getType();
            Gson gson = new Gson();
            promocion = gson.fromJson(respuesta.getContenido(), tipoListaPromo);
        }
        return promocion;
    }
    
    public static Mensaje subirFotoPromocion(Integer idPromocion, byte[] foto){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "promocion/subirFoto/" + idPromocion;
        RespuestaHTTP respuesta = ConexionWS.peticionPUTImagen(url, foto);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Hubo un error al enviar la fotografía de la promoción, por favor inténtalo más tarde");   
        }
        return msj;
    }
    
    public static Promocion obtenerFotoPromocion(Integer idPromocion){
        Promocion promocion = null;
        String url = Constantes.URL_WS + "promocion/obtenerFoto/" + idPromocion;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            promocion = gson.fromJson(respuesta.getContenido(), Promocion.class);
        }
        return promocion;
    }
}
