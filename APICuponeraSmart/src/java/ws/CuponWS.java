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
import modelo.CuponDAO;
import modelo.pojo.Cupon;
import modelo.pojo.Mensaje;
import modelo.pojo.Promocion;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

@Path("cupon")
public class CuponWS {
    
    @Context
    private UriInfo context;
    
    public CuponWS(){
    }
    
    @GET
    @Path("obtenerCuponPorId/{idCupon}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cupon getCuponPromocion(@PathParam("idCupon") Integer idCupon){
        Cupon cupon = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                cupon = conexionBD.selectOne("cupon.obtenerCuponPorId", idCupon);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return cupon;
    }
    
    @Path("obtenerCupones/{idCupon}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cupon> obtenerCupon(Integer idCupon){
        
        return CuponDAO.obtenerCupon(idCupon);
    }
    
    @GET
    @Path("obtenerNumeroCupones/{idPromocion}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cupon getNumeroCupones(@PathParam("idPromocion") Integer idPromocion){
        Cupon cupon = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                cupon = conexionBD.selectOne("cupon.obtenerNumeroCupones", idPromocion);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return cupon;
    }
    
    @GET
    @Path("obtenerCodigoPromocion/{idPromocion}")
    @Produces(MediaType.APPLICATION_JSON)
    public Promocion getCodigoPromocion(@PathParam("idPromocion") Integer idPromocion){
        Promocion promocion = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                promocion = conexionBD.selectOne("promocion.obtenerCodigoPromocion", idPromocion);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return promocion;
    }
    
    @PUT
    @Path("decrementarNumeroCupones")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarCupon(@FormParam("idCupon") Integer idCupon) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if (conexionBD != null) {
            try {
                // Obtener el cupón actual
                Cupon cuponEdicion = conexionBD.selectOne("cupon.obtenerCuponPorId", idCupon);
                if (cuponEdicion != null && cuponEdicion.getCuponesDisponibles() > 0) {
                    int numFilasAfectadas = conexionBD.update("cupon.decrementarNumeroCupones", idCupon);
                    conexionBD.commit();
                    if (numFilasAfectadas > 0) {
                        msj.setError(false);
                        msj.setMensaje("Los datos del cupón han sido actualizados correctamente");
                    } else {
                        msj.setError(true);
                        msj.setMensaje("Por el momento no se pueden actualizar los datos del cupón");
                    }
                } else {
                    msj.setError(true);
                    msj.setMensaje("El cupón no existe o no tiene cupones disponibles para actualizar");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión con la base de datos");
        }
        return msj;
    }

    @PUT
    @Path("actualizarEstadoInactivo")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarEstatusPromocion(@FormParam("idPromocion") Integer idPromocion) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if (conexionBD != null) {
            try {
                // Ejecuta la consulta para actualizar el estado de la promoción a inactivo
                int numFilasAfectadas = conexionBD.update("promocion.actualizarEstadoInactivo", idPromocion);
                conexionBD.commit();
                if (numFilasAfectadas > 0) {
                    msj.setError(false);
                    msj.setMensaje("El estado de la promoción ha sido actualizado correctamente");
                } else {
                    msj.setError(true);
                    msj.setMensaje("No se actualizó el estado de la promoción");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión con la base de datos");
        }
        return msj;
    }
    
    @PUT
    @Path("canjearCupon")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje canjearCuponPromocion(@FormParam("idPromocion") Integer idPromocion, 
                                         @FormParam("codigoPromocion") String codigoPromocion) {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idPromocion", idPromocion);
        parametros.put("codigoPromocion", codigoPromocion);
        
        Mensaje msj = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if(conexionBD != null) {
            try{
                Promocion promocion = conexionBD.selectOne("promocion.obtenerPromoPorId", idPromocion);
                //System.out.println("ID de la promoción recibido: " + idPromocion); 
                if (promocion != null) {
                    Cupon cupon = conexionBD.selectOne("cupon.obtenerCuponPorId", promocion.getIdCupon());
                    if (cupon != null) {
                        int numeroCupones = conexionBD.selectOne("cupon.obtenerNumeroCupones", idPromocion);
                        if (numeroCupones > 0) {
                            String codigoPromo = conexionBD.selectOne("promocion.obtenerCodigoPromocion", idPromocion);
                            
                            if (codigoPromocion.equals(codigoPromo)) {
                                msj.setError(false);
                                msj.setMensaje("Código correcto, el canjeo del cupón fue exitoso");
                                // Decrementar el número de cupones disponibles

                                conexionBD.update("cupon.decrementarNumeroCupones", promocion.getIdCupon());
                                numeroCupones--;

                                // Actualizar el estado de la promoción si no hay más cupones disponibles
                                if (numeroCupones == 0) {
                                    conexionBD.update("promocion.actualizarEstadoInactivo", idPromocion);
                                }
                                
                                // Realizar el commit para persistir los cambios
                                conexionBD.commit();
                            } else {
                                msj.setError(true);
                                msj.setMensaje("Código incorrecto, favor de verificarlo");
                            }
                        } else {
                            msj.setError(true);
                            msj.setMensaje("Esta promoción ya no cuenta con cupones disponibles");
                        }
                    } else {
                        msj.setError(true);
                        msj.setMensaje("No se encontró algún cupón con ese ID, favor de verificarlo");
                    }
                } else {
                    msj.setError(true);
                    msj.setMensaje("Por el momento no se encontró la promoción, favor de verificar el ID");
                }
            }catch (Exception e){
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            }finally{
                conexionBD.close();
            }
        } else{
            // Manejar la excepción específica aquí
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexion con la base de datos");
        }
        return msj;
    }
    
    @GET
    @Path("obtenerCuponPromoFecha")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Promocion> obtenerPromocionesConCuponesDisponibles() {
        List<Promocion> promociones = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                promociones = conexionBD.selectList("obtenerCuponPromoFecha");
            } catch (Exception e) {
                // Manejar excepciones...
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        
        return promociones;
    }
    
    @POST
    @Path("registrar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje guardarCupon(@FormParam("cuponesDisponibles") Integer cuponesDisponibles){
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if(conexionBD != null){
            try{
                Cupon cupon = new Cupon();
                cupon.setCuponesDisponibles(cuponesDisponibles);
                    int numFilasAfectadas = conexionBD.insert("cupon.registrar", cupon);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Información del cupón registrada correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Hubo un error al registrar la información, por favor intentelo más tarde");
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
    public Mensaje editarCupon(@FormParam("idCupon") Integer idCupon,
                               @FormParam("cuponesDisponibles") Integer cuponesDisponibles){
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idCupon", idCupon);
        parametros.put("cuponesDisponibles", cuponesDisponibles);
        SqlSession conexionBD = MyBatisUtil.getSession();
        Mensaje msj = new Mensaje();
        if(conexionBD != null){
            try{
                Cupon cuponEdicion = conexionBD.selectOne("cupon.obtenerCuponPorId", idCupon);
            if(cuponEdicion != null){
                    int numFilasAfectadas = conexionBD.update("cupon.editar", parametros);
                    conexionBD.commit();
                    if(numFilasAfectadas > 0){
                        msj.setError(false);
                        msj.setMensaje("Los datos del cupón han sido actualizada correctamente");
                    }else{
                        msj.setError(true);
                        msj.setMensaje("Por el momento no se puede actualizar los datos del cupón");
                    }
                }else{
                    msj.setError(true);
                    msj.setMensaje("El identificador del cupón a editar no existe, favor de verificarlo");
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
    public Mensaje eliminarCupon(@FormParam("idCupon") Integer idCupon){//boolean es un primitivo, hace referencia a un valor
        Mensaje mensaje = new Mensaje();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                Cupon cuponExiste = conexionBD.selectOne("cupon.obtenerCuponPorId", idCupon);
                if(cuponExiste != null){
                    int numFilasAfectadas = conexionBD.delete("cupon.eliminar", idCupon);
                conexionBD.commit();
                if(numFilasAfectadas > 0){
                    mensaje.setError(false);
                    mensaje.setMensaje("Cupón eliminado correctamente");
                }else{
                    mensaje.setError(true);
                    mensaje.setMensaje("Hubo un error al eliminar el cupón");
                }
            }else{
                    mensaje.setError(true);
                    mensaje.setMensaje("El identificador del cupón a eliminar no existe, favor de varificarlo");
                }
            }catch (Exception e){
                mensaje.setError(true);
                mensaje.setMensaje("Error: "+e.getMessage());
            }finally{
                conexionBD.close();
            }
        }else{
            mensaje.setError(true);
            mensaje.setMensaje("Por el momento no hay conexión con la base de datos");
        }
        return mensaje;
    }
}