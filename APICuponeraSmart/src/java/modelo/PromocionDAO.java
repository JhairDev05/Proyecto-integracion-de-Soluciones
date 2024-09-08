package modelo;

import java.util.LinkedHashMap;
import modelo.pojo.Mensaje;
import modelo.pojo.Promocion;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class PromocionDAO {
    
    public static Mensaje registrarFotoPromocion(Integer idPromocion, byte[] foto){
        Mensaje msj = new Mensaje();
        msj.setError(true);
        LinkedHashMap<String, Object> parametros = new LinkedHashMap();
        parametros.put("idPromocion", idPromocion);
        parametros.put("foto", foto);
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                int filasAfectadas = conexionBD.update("promocion.guardarFoto", parametros);
                conexionBD.commit();
                if(filasAfectadas > 0){
                    msj.setError(false);
                    msj.setMensaje("Fotografía de la promoción guardada correctamente.");
                }else{
                    msj.setMensaje("Lo ssentimos hubo un error al intentar guardar la fotografía, "
                            + "por favor intentelo más tarde");
                }
            }catch (Exception e){
                msj.setMensaje("Error: "+e.getMessage());
            }finally{
                conexionBD.close();
            }
        }else{
            msj.setMensaje("Error de conexión, por el momento no se puede registrar la imágen de la promoción");
        }
        return msj;
    }
    
    public static Promocion obtenerFotoPromocion(int idPromocion){
        Promocion promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                promocion = conexionBD.selectOne("promocion.obtenerFoto", idPromocion);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return promocion;
    }
}
