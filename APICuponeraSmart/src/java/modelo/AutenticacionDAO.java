package modelo;

import java.util.HashMap;
import modelo.pojo.RespuestaLoginEscritorio;
import modelo.pojo.Usuario;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class AutenticacionDAO {
    
    public static RespuestaLoginEscritorio verificarInicioSesionEscritorio (String nombreUsuario, String password){
        
        RespuestaLoginEscritorio respuesta = new RespuestaLoginEscritorio();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("nombreUsuario", nombreUsuario);
                parametros.put("password", password);
                Usuario usuarioSesion = conexionBD.selectOne("autenticacion.loginEscritorio", parametros);
                
                if(usuarioSesion != null){
                    respuesta.setError(false);
                    respuesta.setMensaje("Bienvenido(a) " + usuarioSesion.getNombreUsuario() + " al sistema");
                    respuesta.setUsuarioSesion(usuarioSesion);
                }else{
                    respuesta.setMensaje("Nombre de usuario y/o contraseña incorrectos, favor de volver a intentarlo");
                }
            }catch (Exception e){
                respuesta.setMensaje("Error: "+e.getMessage());
            }
        }else{
            respuesta.setMensaje("Error de conexion con la base de datos");
        }
        return respuesta;
    }
}
