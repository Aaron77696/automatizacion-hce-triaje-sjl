/**
 * Excepción específica de dominio (buena práctica: no usar Exception genérica).
 * Se lanza cuando los signos vitales u otros datos clínicos ingresados
 * están fuera de rangos fisiológicamente posibles.
 */
public class DatosInvalidosException extends Exception {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
