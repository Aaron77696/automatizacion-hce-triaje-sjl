/**
 * Medicamento: cubre "CRUD de medicamentos".
 * Persistencia en medicamentos.txt (id|nombre|dosis|stock).
 */
public class Medicamento {

    private static final String ARCHIVO = "medicamentos.txt";

    private String id;
    private String nombre;
    private String dosis;
    private int stock;

    public Medicamento(String id, String nombre, String dosis, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.dosis = dosis;
        this.stock = stock;
    }

    public void crear() throws OperacionInvalidaException {
        if (nombre == null || nombre.isBlank()) {
            throw new OperacionInvalidaException("El nombre del medicamento no puede estar vacío.");
        }
        if (stock < 0) {
            throw new OperacionInvalidaException("El stock no puede ser negativo.");
        }
        if (ArchivoUtil.buscarPorId(ARCHIVO, id) != null) {
            throw new OperacionInvalidaException("Ya existe un medicamento con el id " + id);
        }
        ArchivoUtil.agregarLinea(ARCHIVO, id + "|" + nombre + "|" + dosis + "|" + stock);
    }

    public static String buscar(String id) {
        return ArchivoUtil.buscarPorId(ARCHIVO, id);
    }

    public static boolean modificar(String id, String nuevoNombre, String nuevaDosis, int nuevoStock)
            throws OperacionInvalidaException {
        if (ArchivoUtil.buscarPorId(ARCHIVO, id) == null) {
            throw new OperacionInvalidaException("No existe medicamento con id " + id);
        }
        return ArchivoUtil.actualizarLinea(ARCHIVO, id, id + "|" + nuevoNombre + "|" + nuevaDosis + "|" + nuevoStock);
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }
}
