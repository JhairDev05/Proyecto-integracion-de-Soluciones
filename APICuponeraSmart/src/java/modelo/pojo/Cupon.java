package modelo.pojo;

public class Cupon {
    
    private Integer idCupon;
    private Integer cuponesDisponibles;
    private Integer idPromocion;
    private String nombrePromo;
    private String nombreEmpresa;
    private String nombreSucursal;
    private String estatus;
    private String codigoPromocion;

    public Cupon() {
    }

    public Cupon(Integer idCupon, Integer cuponesDisponibles, Integer idPromocion, String nombrePromo, String nombreEmpresa, String nombreSucursal, String estatus, String codigoPromocion) {
        this.idCupon = idCupon;
        this.cuponesDisponibles = cuponesDisponibles;
        this.idPromocion = idPromocion;
        this.nombrePromo = nombrePromo;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreSucursal = nombreSucursal;
        this.estatus = estatus;
        this.codigoPromocion = codigoPromocion;
    }

    public Integer getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    public Integer getCuponesDisponibles() {
        return cuponesDisponibles;
    }

    public void setCuponesDisponibles(Integer cuponesDisponibles) {
        this.cuponesDisponibles = cuponesDisponibles;
    }

    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Integer idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getNombrePromo() {
        return nombrePromo;
    }

    public void setNombrePromo(String nombrePromo) {
        this.nombrePromo = nombrePromo;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getCodigoPromocion() {
        return codigoPromocion;
    }

    public void setCodigoPromocion(String codigoPromocion) {
        this.codigoPromocion = codigoPromocion;
    }
}