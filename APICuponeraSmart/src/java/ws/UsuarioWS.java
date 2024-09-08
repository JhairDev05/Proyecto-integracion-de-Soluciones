package ws;

import java.util.HashMap;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import modelo.CatalogoDAO;
import modelo.pojo.Empresa;
import modelo.pojo.Mensaje;
import modelo.pojo.Promocion;
import modelo.pojo.Sucursal;
import modelo.pojo.Usuario;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

@Path("usuario")
public class UsuarioWS {
    
    @Context
    private UriInfo context;
    
    public UsuarioWS(){
    }
    
    @GET
    @Path("obtenerUsuarioId/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario getUsuarioById(@PathParam("idUsuario") Integer idUsuario){
        Usuario usuario = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                usuario = conexionBD.selectOne("usuario.obtenerUsuarioId", idUsuario);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return usuario;
    }
    
    @GET
    @Path("usuarioIdRol/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getUsuarioByIdRol(@PathParam("idUsuario") Integer idUsuario){
        List<Usuario> usuario = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                usuario = conexionBD.selectList("usuario.usuarioIdRol", idUsuario);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return usuario;
    }
    
    @GET
    @Path("obtenerEmpresaUsuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empresa> getEmpresaUsuario(@PathParam("idUsuario") Integer idUsuario){
        List<Empresa> empresa = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Usuario usuario = conexionBD.selectOne("usuario.obtenerUsuarioId", idUsuario);
                if(usuario.getIdRol() == 1){
                    empresa = conexionBD.selectList("empresa.obtenerEmpresa");
                }else if(usuario.getIdRol() == 2){
                    empresa = conexionBD.selectList("usuario.obtenerEmpresaUsuario", idUsuario);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return empresa;
    }
    
    
    @GET
    @Path("obtenerSucursalEmpresa/{idEmpresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sucursal> getSucursalByEmpresa(@PathParam("idEmpresa") Integer idEmpresa){
        List<Sucursal> sucursal = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            Empresa empresa = conexionBD.selectOne("empresa.obtenerEmpresaId", idEmpresa);
            try{
                if(empresa != null){
                    sucursal = conexionBD.selectList("usuario.obtenerSucursalEmpresa", idEmpresa);
                }
            }catch (Exception e){
             e.printStackTrace();
            }finally{
             conexionBD.close();
            }
        }
        return sucursal;
    }
    
    @Path("usuarioComercial")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> obtenerRol(){
        
        return CatalogoDAO.obtenerUsuarioComercial();
    }
    
    @Path("obtenerUsuarios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> obtenerUsuario(){
        
        return CatalogoDAO.obtenerUsuario();
    }
    
    @POST
    @Path("registrar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje guardarUsuario(@FormParam("nombre") String nombre,
                                  @FormParam("apellidoPaterno") String apellidoPaterno,
                                  @FormParam("apellidoMaterno") String apellidoMaterno,
                                  @FormParam("curp") String curp,
                                  @FormParam("correo") String correo,
                                  @FormParam("nombreUsuario") String nombreUsuario,
                                  @FormParam("password") String password,
                                  @FormParam("idRol") Integer idRol,
                                  @FormParam("idEmpresa") Integer idEmpresa){
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if(conexionBD != null){
            try{
                int curpExiste = conexionBD.selectOne("usuario.verificarCurpExistente", curp);
                int nombreUsuarioExiste = conexionBD.selectOne("usuario.verificarNombreUsuarioExiste", nombreUsuario);
                if (curpExiste > 0 || nombreUsuarioExiste > 0) {
                    msj.setError(true);
                    msj.setMensaje("El curp y/o el nombre de usuario ya está existen, favor de usar otro");
                }else{
                    Usuario usuario = new Usuario();
                    usuario.setNombre(nombre);
                    usuario.setApellidoPaterno(apellidoPaterno);
                    usuario.setApellidoMaterno(apellidoMaterno);
                    usuario.setCurp(curp);
                    usuario.setCorreo(correo);
                    usuario.setNombreUsuario(nombreUsuario);
                    usuario.setPassword(password);
                    usuario.setIdRol(idRol);
                    usuario.setIdEmpresa(idEmpresa);
                    
                    int numFilasAfectadas = conexionBD.insert("usuario.registrar", usuario);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Información del usuario registrada correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Hubo un error al registrar la información, por favor inténtalo más tarde");
                    }
                }
            }catch (Exception e){
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            }finally{
                conexionBD.close();
            }
        }else{
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión con la base de datos");
        }
        return msj;
    }
    
    @PUT
    @Path("editar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarUsuario(@FormParam("idUsuario") Integer idUsuario, 
                                 @FormParam("nombre") String nombre,
                                 @FormParam("apellidoPaterno") String apellidoPaterno,
                                 @FormParam("apellidoMaterno") String apellidoMaterno,
                                 @FormParam("curp") String curp,
                                 @FormParam("correo") String correo,
                                 @FormParam("nombreUsuario") String nombreUsuario,
                                 @FormParam("password") String password,
                                 @FormParam("idEmpresa") Integer idEmpresa){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idUsuario", idUsuario);
        parametros.put("nombre", nombre);
        parametros.put("apellidoPaterno", apellidoPaterno);
        parametros.put("apellidoMaterno", apellidoMaterno);
        parametros.put("curp", curp);
        parametros.put("correo", correo);
        parametros.put("nombreUsuario", nombreUsuario);
        parametros.put("password", password);
        parametros.put("idEmpresa", idEmpresa);
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Usuario usuarioEdicion = conexionBD.selectOne("usuario.obtenerUsuarioId", idUsuario);
                if(usuarioEdicion != null){
                    int numFilasAfectadas = conexionBD.update("usuario.editar", parametros);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Los datos del usuario han sido actualizados correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Por el momento no se puede actualizar los datos del usuario");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador del usuario a editar no existe, favor de verificarlo");
                }
            }catch (Exception e){
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            }finally{
                conexionBD.close();
            }
        }else{
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión con la base de datos");
        }
        return msj;
    }
    
    @DELETE
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarUsuario(@FormParam("idUsuario") Integer idUsuario){
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Usuario usuarioExiste = conexionBD.selectOne("usuario.obtenerUsuarioId", idUsuario);
                if(usuarioExiste != null){
                    int numFilasAfectadas =conexionBD.delete("usuario.eliminar", idUsuario);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Usuario eliminado correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Hubo un error al eliminar el usuario");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador del usuario no existe, favor de verificarlo");
                }
            }catch (Exception e){
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            }finally{
                conexionBD.close();
            }
        }
        return msj;
    }
    
    @GET
    @Path("buscar/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> buscarPorNombre(@PathParam("nombre") String nombre){
        List<Usuario> usuario = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                usuario = conexionBD.selectList("usuario.buscar", nombre);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return usuario;
    }
}
