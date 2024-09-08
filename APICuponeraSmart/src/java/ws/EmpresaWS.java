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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import modelo.CatalogoDAO;
import modelo.EmpresaDAO;
import modelo.pojo.Empresa;
import modelo.pojo.Mensaje;
import modelo.pojo.Rol;
import modelo.pojo.Sucursal;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

@Path("empresa")
public class EmpresaWS {
    
    @Context
    private UriInfo context;
    
    public EmpresaWS(){
        
    }

    @GET
    @Path("obtenerEmpresaId/{idEmpresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public Empresa getEmpresaById(@PathParam("idEmpresa") Integer idEmpresa){
        Empresa empresa = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
             empresa = conexionBD.selectOne("empresa.obtenerEmpresaId", idEmpresa);
            }catch (Exception e){
             e.printStackTrace();
            }finally{
             conexionBD.close();
            }
        }
        return empresa;
    }
    
    @GET
    @Path("obtenerNombreEmpresa/{idEmpresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public Empresa obtenerNombreEmpresa(@PathParam("idEmpresa") Integer idEmpresa){
        Empresa empresa = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
             empresa = conexionBD.selectOne("empresa.obtenerNombreEmpresa", idEmpresa);
            }catch (Exception e){
             e.printStackTrace();
            }finally{
             conexionBD.close();
            }
        }
        return empresa;
    }
    
    @GET
    @Path("obtenerEmpresaByIdUsuario/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empresa> getEmpresaByIdUsuario(@PathParam("idUsuario") Integer idUsuario){
        List<Empresa> empresa = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD != null){
            try{
             empresa = conexionBD.selectList("empresa.obtenerEmpresaByIdUsuario", idUsuario);
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
                    sucursal = conexionBD.selectList("empresa.obtenerSucursalEmpresa", idEmpresa);
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
    @Path("buscarPorNombreRFC/{nombreEmpresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empresa> buscarEmpresa(@PathParam("nombreEmpresa") String nombreEmpresa){
        List<Empresa> empresa = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                empresa = conexionBD.selectList("empresa.buscarPorNombreRFC", nombreEmpresa);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return empresa;
    }
    
    @Path("obtenerEmpresa")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empresa> obtenerRol(){
        
        return EmpresaDAO.obtenerEmpresa();
    }
    
    @POST
    @Path("registrar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje guardarEmpresa(@FormParam("nombreEmpresa") String nombreEmpresa,
                                  @FormParam("nombreComercial") String nombreComercial,
                                  @FormParam("nombreRepresentante") String nombreRepresentante,
                                  @FormParam("email") String email,
                                  @FormParam("telefono") String telefono,
                                  @FormParam("paginaWeb") String paginaWeb,
                                  @FormParam("rfc") String rfc){
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if(conexionBD != null){
            try{
                Empresa empresa = new Empresa();
                empresa.setNombreEmpresa(nombreEmpresa);
                empresa.setNombreComercial(nombreComercial);
                empresa.setNombreRepresentante(nombreRepresentante);
                empresa.setEmail(email);
                empresa.setTelefono(telefono);
                empresa.setPaginaWeb(paginaWeb);
                empresa.setRfc(rfc);
                //empresa.setIdEstatus(idEstatus);
                
                empresa.setIdEstatus(1);
                
                int numFilasAfectadas = conexionBD.insert("empresa.registrar", empresa);
                conexionBD.commit();
                
                if (numFilasAfectadas > 0) {
                        msj.setError(false);
                        msj.setMensaje("Información de la empresa registrada correctamente.");
                    } else {
                        msj.setError(true);
                        msj.setMensaje("Hubo un error al registrar la información, por favor inténtalo más tarde.");
                    }
            }catch (Exception e){
                msj.setError(true);
                msj.setMensaje("Error: "+e.getMessage());
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
    public Mensaje editarPaciente(@FormParam("idEmpresa") Integer idEmpresa,
                                  @FormParam("nombreEmpresa") String nombreEmpresa,
                                  @FormParam("nombreComercial") String nombreComercial,
                                  @FormParam("nombreRepresentante") String nombreRepresentante,
                                  @FormParam("email") String email,
                                  @FormParam("telefono") String telefono,
                                  @FormParam("paginaWeb") String paginaWeb,
                                  @FormParam("idEstatus") Integer idEstatus){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idEmpresa", idEmpresa);
        parametros.put("nombreEmpresa", nombreEmpresa);
        parametros.put("nombreComercial", nombreComercial);
        parametros.put("nombreRepresentante", nombreRepresentante);
        parametros.put("email", email);
        parametros.put("telefono", telefono);
        parametros.put("paginaWeb", paginaWeb);
        parametros.put("idEstatus", idEstatus);
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Empresa empresaEdicion = conexionBD.selectOne("empresa.obtenerEmpresaId", idEmpresa);
                if(empresaEdicion != null){
                    int numFilasAfectadas = conexionBD.update("empresa.editar", parametros);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Los datos de la empresa han sido actualizada correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Por el momento no se puede actualizar los datos de la empresa");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador de la empresa a editar no existe, favor de verificarlo");
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
    public Mensaje eliminarEmpresa(@FormParam("idEmpresa") Integer idEmpresa){
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Empresa empresaExiste = conexionBD.selectOne("empresa.obtenerEmpresaId", idEmpresa);
                if(empresaExiste != null){
                    // Verificar si la empresa tiene sucursales asociadas
                    List<Sucursal> sucursales = conexionBD.selectList("sucursal.obtenerSucursalesEmpresa", idEmpresa);
                    if(sucursales.isEmpty()){
                        // Si la empresa no tiene sucursales, eliminarla
                        int numFilasAfectadas = conexionBD.delete("empresa.eliminar", idEmpresa);
                        conexionBD.commit();
                        if(numFilasAfectadas > 0){
                            msj.setError(false);
                            msj.setMensaje("Empresa eliminada correctamente");
                        }else{
                            msj.setError(true);
                            msj.setMensaje("Hubo un error al eliminar la empresa");
                        }
                    }else{
                        // Si la empresa tiene sucursales, mostrar un mensaje de error
                        msj.setError(true);
                        msj.setMensaje("No se puede eliminar la empresa, ya que tiene sucursales asociadas");
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
    @Path("buscarPorNombre/{nombreEmpresa}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Empresa> buscarPorNombre(@PathParam("nombreEmpresa") String nombreEmpresa){
        List<Empresa> empresa = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                empresa = conexionBD.selectList("empresa.buscarPorNombreRFC", nombreEmpresa);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return empresa;
    }
    
    @Path("subirFoto/{idEmpresa}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje subirFotoEmpresa(@PathParam("idEmpresa") Integer idEmpresa, byte[] foto){
        Mensaje msj = new Mensaje();
        if(idEmpresa > 0 && foto != null){
            msj = EmpresaDAO.registrarFotoEmpresa(idEmpresa, foto);
        }else{
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return msj;
    }
    
    @Path("obtenerFoto/{idEmpresa}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Empresa obtenerFotoEmpresa(@PathParam("idEmpresa") Integer idEmpresa){
        Empresa empresa = null;
        if(idEmpresa != null && idEmpresa > 0){
            empresa = EmpresaDAO.obtenerFotoEmpresa(idEmpresa);
        }else{
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return empresa;
    }
}