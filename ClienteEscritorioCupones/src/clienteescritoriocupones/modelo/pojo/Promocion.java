package clienteescritoriocupones.modelo.pojo;

public class Promocion {
    
    private Integer idPromocion;
    private String nombrePromo;
    private String descripcion;
    private byte[] foto;
    private String fechaInicioPromo;
    private String fechaFinPromo;
    private String restricciones;
    private Float cantidadRebajada;
    private Integer cuponesDisponibles;
    private String codigoPromocion;
    private Integer idSucursal;
    private Integer idTipoPromo;
    private Integer idCategoria;
    private Integer idEmpresa;
    private Integer idEstatus;
    private String nombreEmpresa;
    private String nombrePromocion;
    private String nombreCategoria;
    private String estatus;
    private String fotoBase64;
    private Integer idUsuario;
    private Integer idCupon;

    public Promocion() {
    }

    public Promocion(Integer idPromocion, String nombrePromo, String descripcion, byte[] foto, String fechaInicioPromo, String fechaFinPromo, String restricciones, Float cantidadRebajada, Integer cuponesDisponibles, String codigoPromocion, Integer idSucursal, Integer idTipoPromo, Integer idCategoria, Integer idEmpresa, Integer idEstatus, String nombreEmpresa, String nombrePromocion, String nombreCategoria, String estatus, String fotoBase64, Integer idUsuario, Integer idCupon) {
        this.idPromocion = idPromocion;
        this.nombrePromo = nombrePromo;
        this.descripcion = descripcion;
        this.foto = foto;
        this.fechaInicioPromo = fechaInicioPromo;
        this.fechaFinPromo = fechaFinPromo;
        this.restricciones = restricciones;
        this.cantidadRebajada = cantidadRebajada;
        this.cuponesDisponibles = cuponesDisponibles;
        this.codigoPromocion = codigoPromocion;
        this.idSucursal = idSucursal;
        this.idTipoPromo = idTipoPromo;
        this.idCategoria = idCategoria;
        this.idEmpresa = idEmpresa;
        this.idEstatus = idEstatus;
        this.nombreEmpresa = nombreEmpresa;
        this.nombrePromocion = nombrePromocion;
        this.nombreCategoria = nombreCategoria;
        this.estatus = estatus;
        this.fotoBase64 = fotoBase64;
        this.idUsuario = idUsuario;
        this.idCupon = idCupon;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFechaInicioPromo() {
        return fechaInicioPromo;
    }

    public void setFechaInicioPromo(String fechaInicioPromo) {
        this.fechaInicioPromo = fechaInicioPromo;
    }

    public String getFechaFinPromo() {
        return fechaFinPromo;
    }

    public void setFechaFinPromo(String fechaFinPromo) {
        this.fechaFinPromo = fechaFinPromo;
    }

    public String getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(String restricciones) {
        this.restricciones = restricciones;
    }

    public Float getCantidadRebajada() {
        return cantidadRebajada;
    }

    public void setCantidadRebajada(Float cantidadRebajada) {
        this.cantidadRebajada = cantidadRebajada;
    }

    public Integer getCuponesDisponibles() {
        return cuponesDisponibles;
    }

    public void setCuponesDisponibles(Integer cuponesDisponibles) {
        this.cuponesDisponibles = cuponesDisponibles;
    }

    public String getCodigoPromocion() {
        return codigoPromocion;
    }

    public void setCodigoPromocion(String codigoPromocion) {
        this.codigoPromocion = codigoPromocion;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdTipoPromo() {
        return idTipoPromo;
    }

    public void setIdTipoPromo(Integer idTipoPromo) {
        this.idTipoPromo = idTipoPromo;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombrePromocion() {
        return nombrePromocion;
    }

    public void setNombrePromocion(String nombrePromocion) {
        this.nombrePromocion = nombrePromocion;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }
    
    @Override
    public String toString(){
        return "- " + nombrePromo;
    }
}
