import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario {

    private static final String ARCHIVO = "usuarios.txt";

    private int idUsuario;
    private String usuario;
    private String contrasenaHash;
    private String rol;

    public Usuario(int idUsuario, String usuario, String contrasena, String rol) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contrasenaHash = hashear(contrasena);
        this.rol = rol;
    }

    // Protege la contraseña antes de guardarla (no se guarda en texto plano).
    private String hashear(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("[ERROR] No se pudo generar el hash: " + e.getMessage());
            return texto;
        }
    }

    public void registrar() {
        String linea = idUsuario + "|" + usuario + "|" + contrasenaHash + "|" + rol;
        ArchivoUtil.agregarLinea(ARCHIVO, linea);
    }

    public int getIdUsuario() { return idUsuario; }
    public String getUsuario() { return usuario; }
    public String getRol() { return rol; }
}