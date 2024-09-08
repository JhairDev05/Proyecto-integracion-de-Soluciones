package ws;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class AplicationConfig extends Application{
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.AutenticacionWS.class);
        resources.add(ws.CatalogoWS.class);
        resources.add(ws.CuponWS.class);
        resources.add(ws.EmpresaWS.class);
        resources.add(ws.PromocionWS.class);
        resources.add(ws.SucursalWS.class);
        resources.add(ws.UsuarioWS.class);
        
    }
}
