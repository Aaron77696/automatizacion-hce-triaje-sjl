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
        if (ArchivoUtil.existeValor(ARCHIVO, 0, id)) {
            throw new OperacionInvalidaException("Ese ID ya existe.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 2, dni)) {
            throw new OperacionInvalidaException("Ese DNI ya está registrado.");
        }
        ArchivoUtil.agregarLinea(ARCHIVO, ArchivoUtil.unir(id, nombre, dni, tipo.toUpperCase(), especialidad, telefono));
    }

    /** Genera un ID aleatorio con formato Letra+6 dígitos (ej. K482913). */
    public static String nuevoId() {
        return GeneradorId.nuevo(ARCHIVO);
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

    public static boolean existeDoctor(String id) {
        String l = buscar(id);
        if (l == null) return false;
        String[] d = l.split("\\|", -1);
        return d.length == 6 && d[3].equalsIgnoreCase("DOCTOR");
    }

    /** Solo doctores (sin enfermeras), con formato "ID - Nombre", para usar en menús desplegables. */
    public static java.util.List<String> listarDoctoresParaMenu() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (String[] d : listar()) {
            if (d.length == 6 && d[3].equalsIgnoreCase("DOCTOR")) items.add(d[0] + " - " + d[1]);
        }
        return items;
    }

    /** Acepta el ID o el nombre de un doctor y devuelve su ID (o null si no existe). */
    public static String resolverDoctor(String idONombre) throws OperacionInvalidaException {
        if (idONombre == null || idONombre.isBlank()) return null;
        String texto = idONombre.trim();
        String prefijo = texto.contains(" - ") ? texto.substring(0, texto.indexOf(" - ")).trim() : texto; // formato "ID - Nombre"
        for (String[] d : listar()) {
            if (d.length == 6 && d[3].equalsIgnoreCase("DOCTOR")
                    && (d[0].equalsIgnoreCase(texto) || d[0].equalsIgnoreCase(prefijo))) return d[0];
        }
        String encontrado = null;
        for (String[] d : listar()) {
            if (d.length == 6 && d[3].equalsIgnoreCase("DOCTOR") && d[1].equalsIgnoreCase(texto)) {
                if (encontrado != null) {
                    throw new OperacionInvalidaException("Hay varios doctores con ese nombre. Use el ID.");
                }
                encontrado = d[0];
            }
        }
        return encontrado;
    }
}
