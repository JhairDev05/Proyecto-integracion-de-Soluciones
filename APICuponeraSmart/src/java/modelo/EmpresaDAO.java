package modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import modelo.pojo.Empresa;
import modelo.pojo.Mensaje;
import modelo.pojo.Rol;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class EmpresaDAO {
    
    public static List<Empresa> obtenerEmpresa(){
        List<Empresa> empresas = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                empresas = conexionBD.selectList("empresa.obtenerEmpresa");
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return empresas;
    }
    
    public static Empresa obtenerNombreEmpresa(Integer idEmpresa){
        Empresa empresas = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                empresas = conexionBD.selectOne("empresa.obtenerNombreEmpresa", idEmpresa);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return empresas;
    }
    
    public static Mensaje registrarFotoEmpresa(Integer idEmpresa, byte[] foto){
        Mensaje msj = new Mensaje();
        msj.setError(true);
        LinkedHashMap<String, Object> parametros = new LinkedHashMap();
        parametros.put("idEmpresa", idEmpresa);
        parametros.put("foto", foto);
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                int filasAfectadas = conexionBD.update("empresa.guardarFoto", parametros);
                conexionBD.commit();
                if(filasAfectadas > 0){
                    msj.setError(false);
                    msj.setMensaje("Fotografía de la empresa guardada correctamente.");
                }else{
                    msj.setMensaje("Lo sentimos hubo un error al intentar guardar la fotografía, "
                            + "por favor intentelo más tarde");
                }
            }catch (Exception e){
                msj.setMensaje("Error: "+e.getMessage());
            }finally{
                conexionBD.close();
            }
        }else{
            msj.setMensaje("Error de conexión, por el momento no se puede registrar la fotografía de la empresa");
        }
        return msj;
    }
    
    public static Empresa obtenerFotoEmpresa(Integer idEmpresa){
        Empresa empresa = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                empresa = conexionBD.selectOne("empresa.obtenerFoto", idEmpresa);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return empresa;
    }
}


