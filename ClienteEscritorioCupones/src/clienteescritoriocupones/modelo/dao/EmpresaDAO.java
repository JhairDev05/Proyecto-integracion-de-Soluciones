package clienteescritoriocupones.modelo.dao;

import clienteescritoriocupones.modelo.ConexionWS;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Mensaje;
import clienteescritoriocupones.modelo.pojo.RespuestaHTTP;
import clienteescritoriocupones.modelo.pojo.Rol;
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

public class EmpresaDAO {
    
    public static Mensaje registrarEmpresa(Empresa empresaNueva){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"empresa/registrar";
        String parametros = String.format("nombreEmpresa=%s&nombreComercial=%s&nombreRepresentante=%s&email=%s&"
                + "telefono=%s&paginaWeb=%s&rfc=%s", 
                empresaNueva.getNombreEmpresa(), empresaNueva.getNombreComercial(), empresaNueva.getNombreRepresentante(),
                empresaNueva.getEmail(), empresaNueva.getTelefono(), empresaNueva.getPaginaWeb(), empresaNueva.getRfc());
        RespuestaHTTP respuesta = ConexionWS.peticionPOST(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al registrar empresa, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    public static Mensaje editarEmpresa(Empresa empresaEdicion){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"empresa/editar";
        String parametros = String.format("idEmpresa=%s&nombreEmpresa=%s&nombreComercial=%s&nombreRepresentante=%s&"
                + "email=%s&telefono=%s&paginaWeb=%s&idEstatus=%s", 
                empresaEdicion.getIdEmpresa(), empresaEdicion.getNombreEmpresa(), empresaEdicion.getNombreComercial(), 
                empresaEdicion.getNombreRepresentante(), empresaEdicion.getEmail(), empresaEdicion.getTelefono(), 
                empresaEdicion.getPaginaWeb(), empresaEdicion.getIdEstatus());
        RespuestaHTTP respuesta = ConexionWS.peticionPUT(url, parametros);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Error al enviar la información al editar empresa, por favor inténtelo más tarde");
        }
        return msj;
    }
    
    /*
    public static Mensaje eliminarEmpresa(Integer idEmpresa){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"empresa/eliminar";
        String parametros = "idEmpresa=" + idEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionDELETE(url, parametros);
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
            Utilidades.mostrarAlertaSimple("Empresa eliminada", " Empresa eliminada correctamente ", Alert.AlertType.INFORMATION);
        }else{
            Utilidades.mostrarAlertaSimple("Error al eliminar la empresa", respuesta.getContenido(),Alert.AlertType.ERROR);
            }
        return msj;
    }*/
    
    public static Mensaje eliminarEmpresa(Integer idEmpresa){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS+"empresa/eliminar";
        String parametros = "idEmpresa=" + idEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionDELETE(url, parametros);
        if (respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);

            // Verificar si el mensaje indica que la eliminación se realizó correctamente
            if (!msj.isError()) {
                Utilidades.mostrarAlertaSimple("Empresa eliminada", " Empresa eliminada correctamente ", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlertaSimple("Error al eliminar la empresa", msj.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error al eliminar la empresa", respuesta.getContenido(), Alert.AlertType.ERROR);
        }
        return msj;
    }

    
    public static HashMap<String, Object> obtenerEmpresaByUsuario(Integer idUsuario){
        HashMap<String, Object> respService = new LinkedHashMap();
        List<Empresa> empresas = null;
        String url = Constantes.URL_WS+"usuario/obtenerEmpresaUsuario/" + idUsuario;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            Type listaTipoEmpresa = new TypeToken<List<Empresa>>(){}.getType();
            empresas = gson.fromJson(respuesta.getContenido(), listaTipoEmpresa);
            respService.put("error", false);
            respService.put("empresa", empresas);
        }else{
            respService.put("error", true);
            respService.put("mensaje", "Hubo un error en la petición, por el momento no se puede cargar la información de las empresas");
        }
        return respService;
    }
    
    public static List<Empresa> obtenerEmpresaByUsuarioCombo(Integer idUsuario){
        List<Empresa> empresa = new ArrayList<>();
        String url = Constantes.URL_WS+"usuario/obtenerEmpresaUsuario/" + idUsuario;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaEmpresa =  new TypeToken<List<Empresa>>(){}.getType();
            Gson gson = new Gson();
            empresa = gson.fromJson(respuesta.getContenido(), tipoListaEmpresa);
        }
        return empresa;
    }
    
    public static List<Empresa> obtenerEmpresa(){
        List<Empresa> empresas = new ArrayList<>();
        String url = Constantes.URL_WS+"empresa/obtenerEmpresa";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaEmpresa = new TypeToken<List<Empresa>>(){}.getType();
            Gson gson = new Gson();
            empresas = gson.fromJson(respuesta.getContenido(), tipoListaEmpresa);
        }
        return empresas;
    }
    
    public static List<Empresa> consumirServicioBusqueda(String nombreEmpresa){
        List<Empresa> empresas = new ArrayList<>();
        String url = Constantes.URL_WS+"empresa/buscarPorNombreRFC/" + nombreEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaEmpresa =  new TypeToken<List<Empresa>>(){}.getType();
            Gson gson = new Gson();
            empresas = gson.fromJson(respuesta.getContenido(), tipoListaEmpresa);
        }
        return empresas;
    }
    
    public static Empresa obtenerNombreEmpresa(Integer idEmpresa){
        Empresa empresas = null;
        String url = Constantes.URL_WS+"empresa/obtenerNombreEmpresa" + idEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaRol = new TypeToken<List<Empresa>>(){}.getType();
            Gson gson = new Gson();
            empresas = gson.fromJson(respuesta.getContenido(), tipoListaRol);
        }
        return empresas;
    }
    
    public static Mensaje subirFotoEmpresa(Integer idEmpresa, byte[] foto){
        Mensaje msj = new Mensaje();
        String url = Constantes.URL_WS + "empresa/subirFoto/" + idEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionPUTImagen(url, foto);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            msj = gson.fromJson(respuesta.getContenido(), Mensaje.class);
        }else{
            msj.setError(true);
            msj.setMensaje("Hubo un error al enviar la fotografía de la empresa, por favor inténtalo más tarde");   
        }
        return msj;
    }
    
    public static Empresa obtenerFotoEmpresa(Integer idEmpresa){
        Empresa empresa = null;
        String url = Constantes.URL_WS + "empresa/obtenerFoto/" + idEmpresa;
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            empresa = gson.fromJson(respuesta.getContenido(), Empresa.class);
        }
        return empresa;
    }
}
