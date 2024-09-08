package modelo.pojo;

public class RespuestaLoginEscritorio {
    
    private Boolean error;
    private String mensaje;
    private Usuario usuarioSesion;

    public RespuestaLoginEscritorio() {
    }

    public RespuestaLoginEscritorio(Boolean error, String mensaje, Usuario usuarioSesion) {
        this.error = error;
        this.mensaje = mensaje;
        this.usuarioSesion = usuarioSesion;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }
}
