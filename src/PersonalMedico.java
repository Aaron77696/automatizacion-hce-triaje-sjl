/**
 * PersonalMedico: CRUD de doctores/enfermeras.
 * Archivo: personal_medico.txt -> id|nombre|dni|tipo|especialidad|telefono
 */
public class PersonalMedico {

    private static final String ARCHIVO = ArchivoUtil.ruta("personal_medico.txt");

    public static void crear(String id, String nombre, String dni, String tipo, String especialidad, String telefono)
            throws OperacionInvalidaException {
        if (id == null || id.isBlank() || nombre == null || nombre.isBlank() || dni == null || dni.isBlank()) {
            throw new OperacionInvalidaException("ID, nombre y DNI son obligatorios.");
        }
        if (!tipo.equalsIgnoreCase("DOCTOR") && !tipo.equalsIgnoreCase("ENFERMERA") && !tipo.equalsIgnoreCase("OTRO")) {
            throw new OperacionInvalidaException("El tipo debe ser DOCTOR, ENFERMERA u OTRO.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 2, dni)) {
            throw new OperacionInvalidaException("Ese DNI ya está registrado.");
        }
        ArchivoUtil.agregarLinea(ARCHIVO, ArchivoUtil.unir(id, nombre, dni, tipo.toUpperCase(), especialidad, telefono));
    }

    public static String buscar(String id) {
        return ArchivoUtil.buscarPorId(ARCHIVO, id);
    }

    public static boolean modificar(String id, String nombre, String dni, String tipo, String especialidad, String telefono)
            throws OperacionInvalidaException {
        if (ArchivoUtil.buscarPorId(ARCHIVO, id) == null) {
            throw new OperacionInvalidaException("No existe personal médico con id " + id);
        }
        return ArchivoUtil.actualizarLinea(ARCHIVO, id,
                ArchivoUtil.unir(id, nombre, dni, tipo.toUpperCase(), especialidad, telefono));
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }

    public static java.util.List<String[]> listar() {
        return ArchivoUtil.leerComoFilas(ARCHIVO);
    }

    /**
     * Doctores listos para un menú desplegable: "id - nombre (especialidad)".
     * Usa filter (solo DOCTOR, sin enfermeras) y map (arma el texto).
     */
    public static java.util.List<String> listarDoctoresParaMenu() {
        return listar().stream()
                .filter(f -> f.length == 6 && f[3].equalsIgnoreCase("DOCTOR"))
                .map(f -> f[0] + " - " + f[1] + " (" + f[4] + ")")
                .collect(java.util.stream.Collectors.toList());
    }

    public static boolean existeDoctor(String id) {
        String l = buscar(id);
        if (l == null) return false;
        String[] d = l.split("\\|", -1);
        return d.length == 6 && d[3].equalsIgnoreCase("DOCTOR");
    }
}
