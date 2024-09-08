package clienteescritoriocupones.modelo.dao;

import clienteescritoriocupones.modelo.ConexionWS;
import clienteescritoriocupones.modelo.pojo.Categoria;
import clienteescritoriocupones.modelo.pojo.Cupon;
import clienteescritoriocupones.modelo.pojo.Empresa;
import clienteescritoriocupones.modelo.pojo.Estatus;
import clienteescritoriocupones.modelo.pojo.Promocion;
import clienteescritoriocupones.modelo.pojo.RespuestaHTTP;
import clienteescritoriocupones.modelo.pojo.Rol;
import clienteescritoriocupones.modelo.pojo.Sucursal;
import clienteescritoriocupones.modelo.pojo.TipoPromo;
import clienteescritoriocupones.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class CatalogoDAO {
    
    
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
    
    public static List<TipoPromo> obtenerTipoPromo(){
        List<TipoPromo> tipoPromo = new ArrayList<>();
        String url = Constantes.URL_WS+"catalogo/obtenerTipoPromo";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type listaTipoPromo = new TypeToken<List<TipoPromo>>(){}.getType();
            Gson gson = new Gson();
            tipoPromo = gson.fromJson(respuesta.getContenido(), listaTipoPromo);
        }
        return tipoPromo;
    }
    
    public static List<Categoria> obtenerCategoria(){
        List<Categoria> categoria = new ArrayList<>();
        String url = Constantes.URL_WS+"catalogo/obtenerCategoria";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type listaTipoCategoria = new TypeToken<List<Categoria>>(){}.getType();
            Gson gson = new Gson();
            categoria = gson.fromJson(respuesta.getContenido(), listaTipoCategoria);
        }
        return categoria;
    }
    
    public static List<Sucursal> obtenerSucursal(){
        List<Sucursal> sucursal = new ArrayList<>();
        String url = Constantes.URL_WS+"catalogo/obtenerSucursal";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type listaTipoSucursal = new TypeToken<List<Sucursal>>(){}.getType();
            Gson gson = new Gson();
            sucursal = gson.fromJson(respuesta.getContenido(), listaTipoSucursal);
        }
        return sucursal;
    }
    
    public static List<Promocion> obtenerPromocion(){
        List<Promocion> promocion = new ArrayList<>();
        String url = Constantes.URL_WS+"catalogo/obtenerPromocion";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type listaTipoPromocion = new TypeToken<List<Promocion>>(){}.getType();
            Gson gson = new Gson();
            promocion = gson.fromJson(respuesta.getContenido(), listaTipoPromocion);
        }
        return promocion;
    }
    
    public static List<Cupon> obtenerCupon(){
        List<Cupon> cupones = new ArrayList<>();
        String url = Constantes.URL_WS+"catalogo/obtenerCupon";
        RespuestaHTTP respuesta = ConexionWS.peticionGET(url);
        if(respuesta.getCodigoRespuesta() == HttpURLConnection.HTTP_OK){
            Type tipoListaCupon = new TypeToken<List<Cupon>>(){}.getType();
            Gson gson = new Gson();
            cupones = gson.fromJson(respuesta.getContenido(), tipoListaCupon);
        }
        return cupones;
    }
}
