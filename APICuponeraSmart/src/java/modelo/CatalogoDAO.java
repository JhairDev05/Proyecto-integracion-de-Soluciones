package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.pojo.Categoria;
import modelo.pojo.Cupon;
import modelo.pojo.Estatus;
import modelo.pojo.Promocion;
import modelo.pojo.Rol;
import modelo.pojo.Sucursal;
import modelo.pojo.TipoPromo;
import modelo.pojo.Usuario;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class CatalogoDAO {
    
    public static List<Rol> obtenerRol(){
        List<Rol> roles = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                roles = conexionBD.selectList("catalogo.obtenerRol");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return roles;
    }
    
    public static List<Usuario> obtenerUsuario(){
        List<Usuario> usuario = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                usuario = conexionBD.selectList("usuario.obtenerUsuarios");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return usuario;
    }
    
    public static List<Cupon> obtenerCupon(){
        List<Cupon> cupon = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                cupon = conexionBD.selectList("catalogo.obtenerCupon");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return cupon;
    }
    
    public static List<Estatus> obtenerEstatus(){
        List<Estatus> estatus = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                estatus = conexionBD.selectList("catalogo.obtenerEstatus");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return estatus;
    }
    
    public static List<TipoPromo> obtenerTipoPromo(){
        List<TipoPromo> tipoPromo = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                tipoPromo = conexionBD.selectList("catalogo.obtenerTipoPromo");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return tipoPromo;
    }
    
    public static List<Categoria> obtenerCategoria(){
        List<Categoria> categoria = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                categoria = conexionBD.selectList("catalogo.obtenerCategoria");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return categoria;
    }
    
    public static List<Sucursal> obtenerSucursal(){
        List<Sucursal> sucursal = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                sucursal = conexionBD.selectList("catalogo.obtenerSucursal");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return sucursal;
    }
    
    public static List<Usuario> obtenerUsuarioComercial(){
        List<Usuario> usuario = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                usuario = conexionBD.selectList("usuario.usuarioComercial");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return usuario;
    }
    
    public static List<Promocion> obtenerPromocion(){
        List<Promocion> promocion = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                promocion = conexionBD.selectList("catalogo.obtenerPromocion");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return promocion;
    }
}
