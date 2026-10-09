public class Paciente {

    private static final String ARCHIVO = ArchivoUtil.ruta("pacientes.txt");

    public static void crear(String id, String nombre, String apellido, String dni,
                              String fechaNacimiento, String telefono, String direccion)
            throws OperacionInvalidaException {
        if (id == null || id.isBlank() || nombre == null || nombre.isBlank()
                || apellido == null || apellido.isBlank() || dni == null || dni.isBlank()) {
            throw new OperacionInvalidaException("ID, nombres, apellidos y DNI son obligatorios.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 0, id)) {
            throw new OperacionInvalidaException("Ese ID ya existe.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 3, dni)) {
            throw new OperacionInvalidaException("Ese DNI ya está registrado.");
        }
        ArchivoUtil.agregarLinea(ARCHIVO, ArchivoUtil.unir(id, nombre, apellido, dni, fechaNacimiento, telefono, direccion));
    }

    /** Genera un ID aleatorio con formato Letra+6 dígitos (ej. K482913). */
    public static String nuevoId() {
        return GeneradorId.nuevo(ARCHIVO);
    }

    public static String buscar(String idODni) {
        for (String l : ArchivoUtil.leerLineas(ARCHIVO)) {
            String[] d = l.split("\\|", -1);
            if (d.length == 7 && (d[0].equalsIgnoreCase(idODni) || d[3].equalsIgnoreCase(idODni))) {
                d[3] = enmascarar(d[3]);
                return String.join("|", d);
            }
        }
        return null;
    }

    public static boolean modificar(String id, String nombre, String apellido,
                                     String fechaNacimiento, String telefono, String direccion)
            throws OperacionInvalidaException {
        String actual = ArchivoUtil.buscarPorId(ARCHIVO, id);
        if (actual == null) throw new OperacionInvalidaException("No existe paciente con id " + id);
        String dniOriginal = actual.split("\\|", -1)[3];
        return ArchivoUtil.actualizarLinea(ARCHIVO, id,
                ArchivoUtil.unir(id, nombre, apellido, dniOriginal, fechaNacimiento, telefono, direccion));
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }

    public static boolean existeConDni(String dni) {
        return ArchivoUtil.existeValor(ARCHIVO, 3, dni);
    }

    public static java.util.List<String[]> listar() {
        java.util.List<String[]> filas = ArchivoUtil.leerComoFilas(ARCHIVO);
        for (String[] f : filas) {
            if (f.length == 7) f[3] = enmascarar(f[3]);
        }
        return filas;
    }

    private static String enmascarar(String dni) {
        if (dni == null || dni.length() < 4) return "****";
        return dni.substring(0, 3) + "****" + dni.substring(dni.length() - 2);
    }
}
