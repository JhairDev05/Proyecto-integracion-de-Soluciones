package ws;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import modelo.CatalogoDAO;
import modelo.pojo.Categoria;
import modelo.pojo.Cupon;
import modelo.pojo.Estatus;
import modelo.pojo.Promocion;
import modelo.pojo.Rol;
import modelo.pojo.Sucursal;
import modelo.pojo.TipoPromo;

@Path("catalogo")
public class CatalogoWS {
    
    @Path("obtenerRol")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rol> obtenerRol(){
        
        return CatalogoDAO.obtenerRol();
    }
    
    @GET
    @Path("obtenerEstatus")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Estatus> obtenerEstatus(){
        return CatalogoDAO.obtenerEstatus();
    }
    
    @GET
    @Path("obtenerTipoPromo")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoPromo> obtenerTipoPromo(){
        return CatalogoDAO.obtenerTipoPromo();
    }
    
    @GET
    @Path("obtenerCategoria")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Categoria> obtenerCategoria(){
        return CatalogoDAO.obtenerCategoria();
    }
    
    @GET
    @Path("obtenerSucursal")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sucursal> obtenerSucursal(){
        return CatalogoDAO.obtenerSucursal();
    }
    
    @GET
    @Path("obtenerPromocion")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Promocion> obtenerPromocion(){
        return CatalogoDAO.obtenerPromocion();
    }
    
    @GET
    @Path("obtenerCupon")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cupon> obtenerCupon(){
        return CatalogoDAO.obtenerCupon();
    }
}
