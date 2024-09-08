package clienteescritoriocupones.modelo.dao;

import clienteescritoriocupones.modelo.ConexionWS;
import clienteescritoriocupones.modelo.pojo.RespuestaHTTP;
import clienteescritoriocupones.modelo.pojo.RespuestaLogin;
import clienteescritoriocupones.utils.Constantes;
import com.google.gson.Gson;
import java.net.HttpURLConnection;

public class InicioSesionDAO {
    
    public static RespuestaLogin iniciarSesion(String nombreUsuario, String password){
        
        RespuestaLogin respuesta = new RespuestaLogin();
        String url = Constantes.URL_WS + "autenticacion/loginEscritorio";
        String parametros = String.format("nombreUsuario=%s&password=%s", nombreUsuario, password);
        RespuestaHTTP respuestaPeticion = ConexionWS.peticionPOST(url, parametros);
        if(respuestaPeticion.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            respuesta = gson.fromJson(respuestaPeticion.getContenido(), RespuestaLogin.class);
        }else{
            respuesta.setError(true);
            respuesta.setMensaje("Hubo un error al procesar la solicitud, por favor intentelo más tarde");
        }
        return respuesta;
    }
}
