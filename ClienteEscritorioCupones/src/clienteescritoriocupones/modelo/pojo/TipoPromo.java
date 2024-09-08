package clienteescritoriocupones.modelo.pojo;

public class TipoPromo {
    
    private Integer idTipoPromo;
    private String nombrePromocion;

    public TipoPromo() {
    }

    public TipoPromo(Integer tipoPromo, String nombrePromocion) {
        this.idTipoPromo = tipoPromo;
        this.nombrePromocion = nombrePromocion;
    }

    public Integer getIdTipoPromo() {
        return idTipoPromo;
    }

    public void setIdTipoPromo(Integer idTipoPromo) {
        this.idTipoPromo = idTipoPromo;
    }

    public String getNombrePromocion() {
        return nombrePromocion;
    }

    public void setNombrePromocion(String nombrePromocion) {
        this.nombrePromocion = nombrePromocion;
    }
    
    @Override
    public String toString(){
        return "- " + nombrePromocion;
    }
}
