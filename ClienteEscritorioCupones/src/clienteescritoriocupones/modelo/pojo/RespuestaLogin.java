package clienteescritoriocupones.modelo.pojo;

public class RespuestaLogin {
    
    private boolean error;
    private String mensaje;
    private Usuario usuarioSesion;

    public RespuestaLogin() {
    }

    public RespuestaLogin(boolean error, String mensaje, Usuario usuarioSesion) {
        this.error = error;
        this.mensaje = mensaje;
        this.usuarioSesion = usuarioSesion;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
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
