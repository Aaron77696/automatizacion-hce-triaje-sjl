import java.security.MessageDigest;
import java.util.Base64;

public class Usuario {

    private static final String ARCHIVO = ArchivoUtil.ruta("usuarios.txt");
    private static final int MAX_INTENTOS = 3;

    public static void crear(String id, String nombre, String usuario, String claveTexto, String rol)
            throws OperacionInvalidaException {
        if (id == null || id.isBlank() || nombre == null || nombre.isBlank()
                || usuario == null || usuario.isBlank() || claveTexto == null || claveTexto.isBlank()) {
            throw new OperacionInvalidaException("Todos los campos son obligatorios.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 0, id)) {
            throw new OperacionInvalidaException("Ese ID ya existe.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 2, usuario)) {
            throw new OperacionInvalidaException("Ese nombre de usuario ya existe.");
        }
        ArchivoUtil.agregarLinea(ARCHIVO, ArchivoUtil.unir(id, nombre, usuario, hash(claveTexto), rol.toUpperCase()));
    }

    /** Genera un ID aleatorio con formato Letra+6 dígitos (ej. K482913). */
    public static String nuevoId() {
        return GeneradorId.nuevo(ARCHIVO);
    }

    public static String buscar(String idOUsuario) {
        for (String l : ArchivoUtil.leerLineas(ARCHIVO)) {
            String[] d = l.split("\\|", -1);
            if (d.length == 5 && (d[0].equalsIgnoreCase(idOUsuario) || d[2].equalsIgnoreCase(idOUsuario))) {
                return l;
            }
        }
        return null;
    }

    public static boolean modificar(String id, String nuevoNombre, String nuevoUsuario, String nuevaClaveTexto, String nuevoRol)
            throws OperacionInvalidaException {
        String actual = ArchivoUtil.buscarPorId(ARCHIVO, id);
        if (actual == null) throw new OperacionInvalidaException("No existe el usuario con id " + id);
        String[] d = actual.split("\\|", -1);
        String hashFinal = (nuevaClaveTexto == null || nuevaClaveTexto.isBlank()) ? d[3] : hash(nuevaClaveTexto);
        return ArchivoUtil.actualizarLinea(ARCHIVO, id, ArchivoUtil.unir(id, nuevoNombre, nuevoUsuario, hashFinal, nuevoRol.toUpperCase()));
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }

    public static java.util.List<String[]> listar() {
        return ArchivoUtil.leerComoFilas(ARCHIVO);
    }

    public static boolean iniciarSesion(String usuario, String claveTexto) {
        for (String l : ArchivoUtil.leerLineas(ARCHIVO)) {
            String[] d = l.split("\\|", -1);
            if (d.length == 5 && d[2].equalsIgnoreCase(usuario)) {
                return d[3].equals(hash(claveTexto));
            }
        }
        return false;
    }

    public static int intentosMaximos() {
        return MAX_INTENTOS;
    }

    public static void asegurarAdminPorDefecto() {
        if (ArchivoUtil.leerLineas(ARCHIVO).isEmpty()) {
            ArchivoUtil.agregarLinea(ARCHIVO, ArchivoUtil.unir("1", "Administrador", "admin", hash("admin"), "ADMIN"));
        }
    }

    private static String hash(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo proteger la contraseña.", e);
        }
    }
}
