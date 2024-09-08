package modelo.pojo;

public class Rol {
    
    private Integer idRol;
    private String tipoAdmin;

    public Rol() {
    }

    public Rol(Integer idRol, String tipoAdmin) {
        this.idRol = idRol;
        this.tipoAdmin = tipoAdmin;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getTipoAdmin() {
        return tipoAdmin;
    }

    public void setTipoAdmin(String tipoAdmin) {
        this.tipoAdmin = tipoAdmin;
    }
}
