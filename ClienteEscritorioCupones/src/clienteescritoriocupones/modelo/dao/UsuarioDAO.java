package clienteescritoriocupones.modelo.dao;

import clienteescritoriocupones.modelo.ConexionWS;
import clienteescritoriocupones.modelo.pojo.Estatus;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.RespuestaHTTP;
import clienteescritoriocupones.modelo.pojo.Rol;
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

public class UsuarioDAO {
    
    
    public static Mensaje registrarUsuario(Usuario usuarioNuevo){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"usuario/registrar";
        String parametros = String.format("nombre=%s&apellidoPaterno=%s&apellidoMaterno=%s&curp=%s"
                + "&correo=%s&nombreUsuario=%s&password=%s&idRol=%d&idEmpresa=%d",
                usuarioNuevo.getNombre(), usuarioNuevo.getApellidoPaterno(), usuarioNuevo.getApellidoMaterno(), 
                usuarioNuevo.getCurp(), usuarioNuevo.getCorreo(), usuarioNuevo.getNombreUsuario(), 
                usuarioNuevo.getPassword(), usuarioNuevo.getIdRol(), usuarioNuevo.getIdEmpresa());
        RespuestaHTTP respuesta = ConexionWS.peticionPOST(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al registrar al usuario, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static Mensaje editarUsuario(Usuario usuarioEditado){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"usuario/editar";
        String parametros = String.format("idUsuario=%s&nombre=%s&apellidoPaterno=%s&apellidoMaterno=%s&curp=%s"
                + "&correo=%s&nombreUsuario=%s&password=%s&idRol=%d&idEmpresa=%d",
                usuarioEditado.getIdUsuario(), usuarioEditado.getNombre(), usuarioEditado.getApellidoPaterno(), 
                usuarioEditado.getApellidoMaterno(), usuarioEditado.getCurp(), usuarioEditado.getCorreo(), 
                usuarioEditado.getNombreUsuario(), usuarioEditado.getPassword(), usuarioEditado.getIdRol(), 
                usuarioEditado.getIdEmpresa());
        RespuestaHTTP respuesta = ConexionWS.peticionPUT(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al editar al usuario, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static HashMap<String, Object> obtenerUsuariosIdRol(Integer idUsuario){
        HashMap<String, Object> respService = new LinkedHashMap();
        List<Usuario> usuarios = null;
        String url = Constantes.URL_WS+"usuario/usuarioIdRol/" + idUsuario;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            Type listaTipoUsuario = new TypeToken<List<Usuario>>(){}.getType();
            usuarios = gson.fromJson(respuesta.getContenido(), listaTipoUsuario);
            respService.put("error", false);
            respService.put("usuario", usuarios);
        }else{
            respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de las empresas");
        }
        return respService;
    }
    
    public static HashMap<String, Object> obtenerUsuarios(){
        HashMap<String, Object> respService = new LinkedHashMap();
        List<Usuario> usuarios = null;
        String url = Constantes.URL_WS+"usuario/obtenerUsuarios";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            Type listaTipoUsuario = new TypeToken<List<Usuario>>(){}.getType();
            usuarios = gson.fromJson(respuesta.getContenido(), listaTipoUsuario);
            respService.put("error", false);
            respService.put("usuario", usuarios);
        }else{
            respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de los cupones");
        }
        return respService;
    }
    
    public static Mensaje eliminarUsuario(int idUsuario){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"usuario/eliminar";
        String parametros = "idUsuario=" + idUsuario;
        RespuestaHTTP respuesta= ConexionWS.peticionDELETE(url, parametros);
        
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            
            Utilidades.mostrarAlertaSimple("Usuario eliminado", " Usuario eliminado correctamente ", Alert.AlertType.INFORMATION);
        }else{
            Utilidades.mostrarAlertaSimple("Error al eliminar el usuario", respuesta.getContenido(),Alert.AlertType.ERROR);
            }
        return msj;
    }
    
    public static List<Rol> obtenerRol(){
        List<Rol> roles = new ArrayList<>();
        String url = Constantes.URL_WS+"catalogo/obtenerRol";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaRol = new TypeToken<List<Rol>>(){}.getType();
            Gson gson = new Gson();
            roles = gson.fromJson(respuesta.getContenido(), tipoListaRol);
        }
        return roles;
    }
    
    public static List<Estatus> obtenerEstatus(){
        List<Estatus> estatus = new ArrayList<>();
        String url = Constantes.URL_WS+"catalogo/obtenerEstatus";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaEstatus = new TypeToken<List<Estatus>>(){}.getType();
            Gson gson = new Gson();
            estatus = gson.fromJson(respuesta.getContenido(), tipoListaEstatus);
        }
        return estatus;
    }
    
    public static List<Usuario> obtenerUsuario(){
        List<Usuario> usuario = new ArrayList<>();
        String url = Constantes.URL_WS+"usuario/usuarioComercial";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaUsuario = new TypeToken<List<Usuario>>(){}.getType();
            Gson gson = new Gson();
            usuario = gson.fromJson(respuesta.getContenido(), tipoListaUsuario);
        }
        return usuario;
    }
}
