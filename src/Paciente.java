/**
 * Paciente: cubre "CRUD de pacientes" (Crear, Buscar, Modificar, Eliminar).
 * Persistencia en pacientes.txt (id|nombre|apellido|documento|edad).
 */
import java.util.List;
import java.util.stream.Collectors;
public class Paciente {

    private static final String ARCHIVO = "pacientes.txt";

    private String id;
    private String nombre;
    private String apellido;
    private String documento;
    private int edad;

    public static List<String> listarPacientesMayoresDeEdad() {
    return ArchivoUtil.leerLineas(ARCHIVO).stream()
            .filter(linea -> Integer.parseInt(linea.split("\\|")[4]) >= 18)
            .map(linea -> {
                String[] p = linea.split("\\|");
                return p[1] + " " + p[2] + " (edad: " + p[4] + ")";
            })
            .collect(Collectors.toList());
}
    public Paciente(String id, String nombre, String apellido, String documento, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.edad = edad;
    }

    public void crear() throws OperacionInvalidaException {
        if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank()) {
            throw new OperacionInvalidaException("Nombre y apellido del paciente son obligatorios.");
        }
        if (ArchivoUtil.buscarPorId(ARCHIVO, id) != null) {
            throw new OperacionInvalidaException("Ya existe un paciente con el id " + id);
        }
        ArchivoUtil.agregarLinea(ARCHIVO, id + "|" + nombre + "|" + apellido + "|" + documento + "|" + edad);
    }

    /** Buscar: devuelve el registro con el documento enmascarado (Ley N.° 29733). */
    public static String buscar(String id) {
        String linea = ArchivoUtil.buscarPorId(ARCHIVO, id);
        if (linea == null) return null;
        String[] partes = linea.split("\\|");
        String documentoEnmascarado = enmascarar(partes[3]);
        return partes[0] + "|" + partes[1] + "|" + partes[2] + "|" + documentoEnmascarado + "|" + partes[4];
    }

    public static boolean modificar(String id, String nuevoNombre, String nuevoApellido, int nuevaEdad)
            throws OperacionInvalidaException {
        String actual = ArchivoUtil.buscarPorId(ARCHIVO, id);
        if (actual == null) {
            throw new OperacionInvalidaException("No existe paciente con id " + id);
        }
        String documentoOriginal = actual.split("\\|")[3]; // se conserva, no se pierde al modificar
        return ArchivoUtil.actualizarLinea(ARCHIVO, id, id + "|" + nuevoNombre + "|" + nuevoApellido + "|" + documentoOriginal + "|" + nuevaEdad);
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }

    private static String enmascarar(String documento) {
        if (documento == null || documento.length() < 4) return "****";
        return documento.substring(0, 3) + "****" + documento.substring(documento.length() - 2);
    }
}
