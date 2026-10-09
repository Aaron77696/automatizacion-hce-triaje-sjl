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
}
