package clienteescritoriocupones.modelo.dao;

import clienteescritoriocupones.modelo.ConexionWS;
import clienteescritoriocupones.modelo.pojo.Cupon;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.Promocion;
import clienteescritoriocupones.modelo.pojo.RespuestaHTTP;
import clienteescritoriocupones.modelo.pojo.Usuario;
import clienteescritoriocupones.utils.Constantes;
import clienteescritoriocupones.utils.Utilidades;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javafx.scene.control.Alert;

public class CuponDAO {
    
    public static Mensaje canjearCupon(Cupon canjearCupon){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"cupon/canjearCupon";
        String parametros = String.format("idPromocion=%s&codigoPromocion=%s", 
                canjearCupon.getIdPromocion(), canjearCupon.getCodigoPromocion());
        RespuestaHTTP respuesta = ConexionWS.peticionPUT(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al canjear el cupón, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static HashMap<String, Object> obtenerCupones(Integer idCupon){
        HashMap<String, Object> respService = new LinkedHashMap();
        List<Cupon> cupones = null;
        String url = Constantes.URL_WS+"cupon/obtenerCupones/" + idCupon;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            Type listaTipoCupon = new TypeToken<List<Cupon>>(){}.getType();
            cupones = gson.fromJson(respuesta.getContenido(), listaTipoCupon);
            respService.put("error", false);
            respService.put("cupon", cupones);
        }else{
            respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de los cupones");
        }
        return respService;
    }
    
    public static Mensaje registrarCupon(Cupon cuponNueva){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"cupon/registrar";
        String parametros = String.format("cuponesDisponibles=%s", 
                cuponNueva.getCuponesDisponibles());
        RespuestaHTTP respuesta = ConexionWS.peticionPOST(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al registrar cupón, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static Mensaje editarCupon(Cupon cuponEditado){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"cupon/editar";
        String parametros = String.format("idCupon=%s&cuponesDisponibles=%s", 
                cuponEditado.getIdCupon(), cuponEditado.getCuponesDisponibles());
        RespuestaHTTP respuesta = ConexionWS.peticionPUT(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al editar cupón, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static Mensaje eliminarCupon(Integer idCupon){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"cupon/eliminar";
        String parametros = "idCupon=" + idCupon;
        RespuestaHTTP respuesta= ConexionWS.peticionDELETE(url, parametros);
        
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            
            Utilidades.mostrarAlertaSimple("Cupón eliminado", " Cupón eliminado correctamente ", Alert.AlertType.INFORMATION);
        }else{
            Utilidades.mostrarAlertaSimple("Error al eliminar el cupón", respuesta.getContenido(),Alert.AlertType.ERROR);
            }
        return msj;
    }
}
