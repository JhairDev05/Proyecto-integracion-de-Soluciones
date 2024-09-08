package ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import modelo.PromocionDAO;
import modelo.pojo.Categoria;
import modelo.pojo.Cupon;
import modelo.pojo.Mensaje;
import modelo.pojo.Promocion;
import modelo.pojo.Sucursal;
import modelo.pojo.Usuario;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

@Path("promocion")
public class PromocionWS {
    
    @Context
    private UriInfo context;
    
    public PromocionWS(){
    }
    
    @GET
    @Path("obtenerPromoPorId/{idPromocion}")
    @Produces(MediaType.APPLICATION_JSON)
    public Promocion getPromocionById(@PathParam("idPromocion") Integer idPromocion){
        Promocion promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                promocion = conexionBD.selectOne("promocion.obtenerPromoPorId", idPromocion);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return promocion;
    }
    
    @GET
    @Path("obtenerPromoSucursal/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Promocion> getEmpresaUsuario(@PathParam("idUsuario") Integer idUsuario){
        List<Promocion> promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Usuario usuario = conexionBD.selectOne("usuario.obtenerUsuarioId", idUsuario);
                if(usuario.getIdRol() == 1){
                    promocion = conexionBD.selectList("promocion.obtenerPromocion");
                }else if(usuario.getIdRol() == 2){
                    promocion = conexionBD.selectList("promocion.obtenerPromoSucursal", usuario.getIdEmpresa());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return promocion;
    }
    
    @GET 
    @Path("buscarPromocion/{nombrePromo}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Promocion> buscarPromocion(@PathParam("nombrePromo") String nombrePromo){
        List<Promocion> promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                promocion = conexionBD.selectList("promocion.buscarPromocion", nombrePromo);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return promocion;
    }
    
    @GET
    @Path("obtenerPromoCategoria/{idCategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Promocion> getPromocionByCategoria(@PathParam("idCategoria") Integer idCategoria){
        List<Promocion> promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            Categoria categoria = conexionBD.selectOne("promocion.obtenerCategoriaId", idCategoria);
            try{
                if(categoria != null){
                    promocion = conexionBD.selectList("promocion.obtenerPromoCategoria", idCategoria);
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
    public Mensaje guardarPromocion(@FormParam("nombrePromo") String nombrePromo,
                                    @FormParam("descripcion") String descripcion,
                                    @FormParam("fechaInicioPromo") String fechaInicioPromo,
                                    @FormParam("fechaFinPromo") String fechaFinPromo,
                                    @FormParam("restricciones") String restricciones,
                                    @FormParam("cantidadRebajada") Float cantidadRebajada,
                                    @FormParam("idSucursal") Integer idSucursal,
                                    @FormParam("idTipoPromo") Integer idTipoPromo,
                                    @FormParam("idCategoria") Integer idCategoria,
                                    @FormParam("idEmpresa") Integer idEmpresa,
                                    @FormParam("cuponesDisponibles") Integer cuponesDisponibles) {

        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();

        if (conexionBD != null) {
            try {
                String codigoPromocion = generarCodigoAleatorio();

                // Insertar el cupón primero
                Cupon cupon = new Cupon();
                cupon.setCuponesDisponibles(cuponesDisponibles);

                int numFilasAfectadasCupon = conexionBD.insert("cupon.registrar", cupon);

                if (numFilasAfectadasCupon > 0) {
                    // Obtener el idCupon generado automáticamente
                    int idCuponGenerado = cupon.getIdCupon(); // Asumiendo que obtienes el ID generado
                    // Insertar la promoción asociada con el idCupon obtenido
                    Promocion promo = new Promocion();
                    promo.setNombrePromo(nombrePromo);
                    promo.setDescripcion(descripcion);
                    promo.setFechaInicioPromo(fechaInicioPromo);
                    promo.setFechaFinPromo(fechaFinPromo);
                    promo.setRestricciones(restricciones);
                    promo.setCodigoPromocion(codigoPromocion);
                    promo.setCantidadRebajada(cantidadRebajada);
                    promo.setIdSucursal(idSucursal);
                    promo.setIdTipoPromo(idTipoPromo);
                    promo.setIdCategoria(idCategoria);
                    promo.setIdEmpresa(idEmpresa);
                    promo.setIdEstatus(1);
                    promo.setIdCupon(idCuponGenerado); // Asignar el idCupon obtenido

                    int numFilasAfectadasPromo = conexionBD.insert("promocion.registrar", promo);

                    if (numFilasAfectadasPromo > 0) {
                        conexionBD.commit();
                        msj.setError(false);
                        msj.setMensaje("Información de la promoción y cupones registrada correctamente.");
                    } else {
                        conexionBD.rollback();
                        msj.setError(true);
                        msj.setMensaje("Hubo un error al registrar la información de la promoción, por favor inténtelo más tarde.");
                    }
                } else {
                    msj.setError(true);
                    msj.setMensaje("Hubo un error al registrar la información del cupón, por favor inténtelo más tarde.");
                }
            } catch (Exception e) {
                e.printStackTrace(); // Imprimir el rastro de la excepción en la consola
                msj.setError(true);
                msj.setMensaje("Error al procesar la solicitud.");
            } finally {
                conexionBD.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión con la base de datos");
        }
        return msj;
    }



    
    public String generarCodigoAleatorio(){
        int longitud = 8;
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder(longitud);

        for(int i=0; i<longitud; i++){
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }
    
    @PUT
    @Path("editar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarPromocion(@FormParam("idPromocion") Integer idPromocion, 
                                   @FormParam("nombrePromo") String nombrePromo,
                                   @FormParam("descripcion") String descripcion,
                                   @FormParam("fechaInicioPromo") String fechaInicioPromo,
                                   @FormParam("fechaFinPromo") String fechaFinPromo, 
                                   //@FormParam("numeroCupones") Integer numeroCupones,
                                   @FormParam("restricciones") String restricciones, 
                                   @FormParam("cantidadRebajada") Float cantidadRebajada,
                                   @FormParam("codigoPromocion") String codigoPromocion,
                                   @FormParam("idSucursal") Integer idSucursal, 
                                   @FormParam("idTipoPromo") Integer idTipoPromo, 
                                   @FormParam("idCategoria") Integer idCategoria, 
                                   @FormParam("idEmpresa") Integer idEmpresa,
                                   @FormParam("idEstatus") Integer idEstatus,
                                   //@FormParam("idCupon") Integer idCupon,
                                   @FormParam("idSucursales") List<Integer> idSucursales){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idPromocion", idPromocion);
        parametros.put("nombrePromo", nombrePromo);
        parametros.put("descripcion", descripcion);
        parametros.put("fechaInicioPromo", fechaInicioPromo);
        parametros.put("fechaFinPromo", fechaFinPromo);
        //parametros.put("numeroCupones", numeroCupones);
        parametros.put("restricciones", restricciones);
        parametros.put("cantidadRebajada", cantidadRebajada);
        parametros.put("codigoPromocion", codigoPromocion);
        parametros.put("idSucursal", idSucursal);
        parametros.put("idTipoPromo", idTipoPromo);
        parametros.put("idCategoria", idCategoria);
        parametros.put("idEmpresa", idEmpresa);
        parametros.put("idEstatus", idEstatus);
        //parametros.put("idCupon", idCupon);
        
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD != null){
            try{
                Promocion promoEdicion = conexionBD.selectOne("promocion.obtenerPromoId", idPromocion);
                if(promoEdicion != null){
                    int numFilasAfectadas = conexionBD.update("promocion.editar", parametros);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Los datos de la promoción han sido actualizados correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Por el momento no se pueden actualizar los datos de la promoción");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador de la promoción a editar no existe, favor de verificarlo");
                }
            }catch (Exception e){
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            }finally{
                conexionBD.close();
            }
        }else{
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión a la base de datos");
        }
        return msj;
    }
    
    @DELETE
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarPromocion(@FormParam("idPromocion") Integer idPromocion){
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Promocion promocionExiste = conexionBD.selectOne("promocion.obtenerPromoId", idPromocion);
                if(promocionExiste != null){
                    List<Promocion> promociones = conexionBD.selectList("promocion.obtenerPromocionSucursal", idPromocion);
                    if(promociones.isEmpty()){
                        int numFilasAfectadas = conexionBD.delete("promocion.eliminar", idPromocion);
                        conexionBD.commit();
                        if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Promocion eliminada correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Hubo un problema al eliminar la promoción");
                        }
                    }else{
                        msj.setError(true);
                        msj.setMensaje("No se puede eliminar la promocion, ya que tiene sucursales asociadas");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador de la promoción a eliminar no existe, favor de verificarlo");
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
    @Path("buscar/{nombrePromo}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Promocion> buscarPorNombreFecha(@PathParam("nombrePromo") String nombrePromo){
        List<Promocion> promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                promocion = conexionBD.selectList("promocion.buscar", nombrePromo);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return promocion;
    }
    
    @Path("subirFoto/{idPromocion}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje subirFotoPromocion(@PathParam("idPromocion") Integer idPromocion, byte[] foto){
        Mensaje msj = new Mensaje();
        if(idPromocion > 0 && foto != null){
            msj = PromocionDAO.registrarFotoPromocion(idPromocion, foto);
        }else{
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return msj;
    }
    
    @Path("obtenerFoto/{idPromocion}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Promocion obtenerFotoPromocion(@PathParam("idPromocion") Integer idPromocion){
        Promocion promocion = null;
        if(idPromocion != null && idPromocion > 0){
            promocion = PromocionDAO.obtenerFotoPromocion(idPromocion);
        }else{
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return promocion;
    }
}
