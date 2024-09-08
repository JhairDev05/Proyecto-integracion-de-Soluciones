package clienteescritoriocupones.modelo.pojo;

public class Sucursal {
    
    private Integer idSucursal;
    private String nombreSucursal;
    private String telefono;
    private String nombreEncargado;
    private Integer idEstatus;
    private Integer idEmpresa;
    private String nombreEmpresa;
    private String estatus;

    public Sucursal() {
    }

    public Sucursal(Integer idSucursal, String nombreSucursal, String telefono, String nombreEncargado, Integer idEstatus, Integer idEmpresa, String nombreEmpresa, String estatus) {
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        this.telefono = telefono;
        this.nombreEncargado = nombreEncargado;
        this.idEstatus = idEstatus;
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.estatus = estatus;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public void setNombreEncargado(String nombreEncargado) {
        this.nombreEncargado = nombreEncargado;
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    
    @Override
    public String toString(){
        return "- " + nombreSucursal;
    }
}
