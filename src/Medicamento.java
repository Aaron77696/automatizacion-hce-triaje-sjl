public class Medicamento {

    private static final String ARCHIVO = ArchivoUtil.ruta("medicamentos.txt");

    public static void crear(String id, String nombre, String dosis, String stock, String descripcion)
            throws OperacionInvalidaException {
        if (id == null || id.isBlank() || nombre == null || nombre.isBlank()) {
            throw new OperacionInvalidaException("ID y nombre son obligatorios.");
        }
        int stockNum = parsearStock(stock);
        if (stockNum < 0) {
            throw new OperacionInvalidaException("El stock no puede ser negativo.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 0, id)) {
            throw new OperacionInvalidaException("Ese código ya existe.");
        }
        ArchivoUtil.agregarLinea(ARCHIVO, ArchivoUtil.unir(id, nombre, dosis, String.valueOf(stockNum), descripcion));
    }

    /** Genera un ID aleatorio con formato Letra+6 dígitos (ej. K482913). */
    public static String nuevoId() {
        return GeneradorId.nuevo(ARCHIVO);
    }

    public static String buscar(String id) {
        return ArchivoUtil.buscarPorId(ARCHIVO, id);
    }

    public static boolean modificar(String id, String nombre, String dosis, String stock, String descripcion)
            throws OperacionInvalidaException {
        if (ArchivoUtil.buscarPorId(ARCHIVO, id) == null) {
            throw new OperacionInvalidaException("No existe medicamento con id " + id);
        }
        int stockNum = parsearStock(stock);
        if (stockNum < 0) {
            throw new OperacionInvalidaException("El stock no puede ser negativo.");
        }
        return ArchivoUtil.actualizarLinea(ARCHIVO, id, ArchivoUtil.unir(id, nombre, dosis, String.valueOf(stockNum), descripcion));
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }

    public static java.util.List<String[]> listar() {
        return ArchivoUtil.leerComoFilas(ARCHIVO);
    }

    private static int parsearStock(String stock) throws OperacionInvalidaException {
        try {
            return Integer.parseInt(stock.trim());
        } catch (Exception e) {
            throw new OperacionInvalidaException("El stock debe ser un número entero.");
        }
    }

    /** Acepta el ID o el nombre de un medicamento y devuelve su ID (o null si no existe). */
    public static String resolverId(String idONombre) throws OperacionInvalidaException {
        if (idONombre == null || idONombre.isBlank()) return null;
        String texto = idONombre.trim();
        for (String[] d : listar()) {
            if (d.length == 5 && d[0].equalsIgnoreCase(texto)) return d[0];
        }
        String encontrado = null;
        for (String[] d : listar()) {
            if (d.length == 5 && d[1].equalsIgnoreCase(texto)) {
                if (encontrado != null) {
                    throw new OperacionInvalidaException("Hay varios medicamentos con ese nombre. Use el ID.");
                }
                encontrado = d[0];
            }
        }
        return encontrado;
    }
}
