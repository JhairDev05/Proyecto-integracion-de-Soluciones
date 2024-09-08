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
import modelo.pojo.Empresa;
import modelo.pojo.Mensaje;
import modelo.pojo.Promocion;
import modelo.pojo.Sucursal;
import modelo.pojo.Usuario;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

@Path("sucursal")
public class SucursalWS {
    
    @Context
    private UriInfo context;
    
    public SucursalWS(){
    }
    
    @GET
    @Path("obtenerSucursalId/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Sucursal getSucursalById(@PathParam("idSucursal") Integer idSucursal){
        Sucursal sucursal = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                sucursal = conexionBD.selectOne("sucursal.obtenerSucursalId", idSucursal);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return sucursal;
    }
    
    @GET
    @Path("obtenerSucursalRol/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sucursal> getEmpresaUsuario(@PathParam("idUsuario") Integer idUsuario){
        List<Sucursal> sucursal = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Usuario usuario = conexionBD.selectOne("usuario.obtenerUsuarioId", idUsuario);
                if(usuario.getIdRol() == 1){
                    sucursal = conexionBD.selectList("sucursal.obtenerSucursal");
                }else if(usuario.getIdRol() == 2){
                    sucursal = conexionBD.selectList("empresa.obtenerSucursalEmpresa", usuario.getIdEmpresa());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return sucursal;
    }
    
    
    @GET 
    @Path("buscarNombreSucursal/{nombreSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sucursal> buscarSucursal(@PathParam("nombreSucursal") String nombreSucursal){
        List<Sucursal> sucursal = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                sucursal = conexionBD.selectList("sucursal.buscarNombreSucursal", nombreSucursal);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return sucursal;
    }
    
    @GET
    @Path("obtenerPromoSucursal/{idSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Promocion> getPromocionBySucursal(@PathParam("idSucursal") Integer idSucursal){
        List<Promocion> promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            Sucursal sucursal = conexionBD.selectOne("sucursal.obtenerSucursalId", idSucursal);
            try{
                if(sucursal != null){
                    promocion = conexionBD.selectList("sucursal.obtenerPromoSucursal", idSucursal);
                }
            }catch (Exception e){
             e.printStackTrace();
            }finally{
             conexionBD.close();
            }
        }
        return promocion;
    }

    
    @POST
    @Path("registrar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje guardarEmpresa(@FormParam("nombreSucursal") String nombreSucursal,
                                   @FormParam("telefono") String telefono,
                                   @FormParam("nombreEncargado") String nombreEncargado,
                                   @FormParam("idEstatus") Integer idEstatus,
                                   @FormParam("idEmpresa") Integer idEmpresa){
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if(conexionBD != null){
            try{
                Sucursal sucursal = new Sucursal();
                sucursal.setNombreSucursal(nombreSucursal);
                sucursal.setTelefono(telefono);
                sucursal.setNombreEncargado(nombreEncargado);
                sucursal.setIdEstatus(idEstatus);
                sucursal.setIdEmpresa(idEmpresa);
                
                List<Empresa> empresa = conexionBD.selectList("empresa.obtenerEmpresaId", idEmpresa);
                if(!empresa.isEmpty()){
                    int numFilasAfectadas = conexionBD.insert("sucursal.registrar", sucursal);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Información de la sucursal registrada correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Hubo un error al registrar la información, por favor intentelo más tarde");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("No se puede agregar la sucursal porque el ID de la empresa no exite, "
                            + "favor de verificar el ID de la empresa");
                }
            }catch (Exception e){
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            }
        }else{
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexion con la base de datos");
        }
        return msj;
    }
    
    @PUT
    @Path("editar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarSucursal(@FormParam("idSucursal") Integer idSucursal,
                                  @FormParam("nombreSucursal") String nombreSucursal,
                                  @FormParam("telefono") String telefono,
                                  @FormParam("nombreEncargado") String nombreEncargado,
                                  @FormParam("idEstatus") Integer idEstatus,
                                  @FormParam("idEmpresa") Integer idEmpresa){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idSucursal", idSucursal);
        parametros.put("nombreSucursal", nombreSucursal);
        parametros.put("telefono", telefono);
        parametros.put("nombreEncargado", nombreEncargado);
        parametros.put("idEstatus", idEstatus);
        parametros.put("idEmpresa", idEmpresa);
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if(conexionBD != null){
            try{
                Sucursal sucursalEdicion = conexionBD.selectOne("sucursal.obtenerSucursalId", idSucursal);
                List<Empresa> empresa = conexionBD.selectList("empresa.obtenerEmpresaId", idEmpresa);
            if(sucursalEdicion != null && !empresa.isEmpty()){
                    int numFilasAfectadas = conexionBD.update("sucursal.editar", parametros);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Los datos de la sucurdal han sido actualizada correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Por el momento no se puede actualizar los datos de la sucursal");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador de la sucursal a editar o de la empresa no existe, favor de verificarlos");
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
    public Mensaje elminarSucursal(@FormParam("idSucursal") Integer idSucursal){
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Sucursal sucursalExiste = conexionBD.selectOne("sucursal.obtenerSucursalId", idSucursal);
                if(sucursalExiste != null){
                    // Verificar si la sucursal tiene promociones asociadas
                    List<Promocion> promos = conexionBD.selectList("promocion.obtenerPromosSucursal", idSucursal);
                    if(promos.isEmpty()){
                        // Si la empresa no tiene promociones, eliminarla
                        int numFilasAfectadas = conexionBD.delete("sucursal.eliminar", idSucursal);
                        conexionBD.commit();
                        if(numFilasAfectadas > 0){
                            msj.setError(false);
                            msj.setMensaje("Sucursal eliminada correctamente");
                        }else{
                            msj.setError(true);
                            msj.setMensaje("Hubo un error al eliminar la sucursal");
                        }
                    }else{
                        // Si la empresa tiene sucursales, mostrar un mensaje de error
                        msj.setError(true);
                        msj.setMensaje("No se puede eliminar la sucursal, ya que tiene promociones asociadas");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador de la empresa a eliminar no existe, favor de verificarlo");
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
    @Path("buscarNombreDireccion/{nombreSucursal}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sucursal> buscarPorNombre(@PathParam("nombreSucursal") String nombreSucursal){
        List<Sucursal> sucursales = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                sucursales = conexionBD.selectList("sucursal.buscarNombreDireccion", nombreSucursal);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return sucursales;
    }

}
