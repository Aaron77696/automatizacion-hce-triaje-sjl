import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * GestorSeguridad (SRP): única responsabilidad = proteger datos personales.
 * Da cumplimiento a la Ley N.° 29733 (Ley de Protección de Datos Personales, Perú),
 * evitando que información sensible (DNI, contraseñas) se guarde o muestre en texto plano.
 */
public class GestorSeguridad {

    private static final String ALGORITMO = "SHA-256";

    /**
     * Aplica hashing irreversible a un dato sensible (ej. contraseña).
     * Nunca se guarda el valor original, solo su huella digital (hash).
     */
    public static String hashearDato(String textoPlano) {
        if (textoPlano == null || textoPlano.isBlank()) {
            throw new IllegalArgumentException("El dato a proteger no puede estar vacío.");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO);
            byte[] hashBytes = digest.digest(textoPlano.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            // Manejo de errores: no se expone información interna al usuario final.
            throw new RuntimeException("No se pudo proteger el dato solicitado.", e);
        }
    }

    /**
     * Enmascara un documento de identidad para mostrarlo en pantallas o reportes
     * sin exponer el número completo (ej. 74812345 -> 748****45).
     */
    public static String enmascararDocumento(String documento) {
        if (documento == null || documento.length() < 4) {
            return "****";
        }
        String inicio = documento.substring(0, 3);
        String fin = documento.substring(documento.length() - 2);
        return inicio + "****" + fin;
    }
}
