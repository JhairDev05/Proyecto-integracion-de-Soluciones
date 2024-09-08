package clienteescritoriocupones.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utilidades {
    
    public static void mostrarAlertaSimple(String titulo, String mensaje, Alert.AlertType tipo){
        
        Alert dialogoAlerta = new Alert(tipo);
        dialogoAlerta.setTitle(titulo);
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.setContentText(mensaje);
        dialogoAlerta.showAndWait();
    }
    
    public static Optional<ButtonType> mostrarAlertaConfirmacion(String titulo, String confirmacion){
        Alert dialogoAlertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoAlertaConfirmacion.setTitle(titulo);
        dialogoAlertaConfirmacion.setHeaderText(null);
        dialogoAlertaConfirmacion.setContentText(confirmacion);
        
        return dialogoAlertaConfirmacion.showAndWait();
        
    }
}
