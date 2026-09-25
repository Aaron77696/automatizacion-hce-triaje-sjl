import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ArchivoUtil: utilidad compartida para simular el "bloc de notas" pedido
 * por el docente. Cada entidad (Usuario, Paciente, PersonalMedico,
 * Medicamento, Cita) se guarda como una línea de texto separada por "|"
 * dentro de su propio archivo .txt.
 */
public class ArchivoUtil {

    /** Agrega una línea nueva al final del archivo (Crear). */
    public static void agregarLinea(String archivo, String linea) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            // Manejo de errores: no se detiene el sistema, se informa el fallo de escritura.
            System.out.println("[ERROR] No se pudo guardar el registro: " + e.getMessage());
        }
    }

    /** Lee todas las líneas del archivo (Buscar/Listar). */
    public static List<String> leerLineas(String archivo) {
        List<String> lineas = new ArrayList<>();
        File f = new File(archivo);
        if (!f.exists()) {
            return lineas; // archivo aún no creado: lista vacía, no es un error
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isBlank()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudo leer el archivo: " + e.getMessage());
        }
        return lineas;
    }

    /** Reemplaza la línea cuyo primer campo (id) coincide (Modificar). */
    public static boolean actualizarLinea(String archivo, String id, String nuevaLinea) {
        List<String> lineas = leerLineas(archivo);
        boolean encontrado = false;
        for (int i = 0; i < lineas.size(); i++) {
            if (lineas.get(i).startsWith(id + "|")) {
                lineas.set(i, nuevaLinea);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            reescribirArchivo(archivo, lineas);
        }
        return encontrado;
    }

    /** Elimina la línea cuyo primer campo (id) coincide (Eliminar). */
    public static boolean eliminarLinea(String archivo, String id) {
        List<String> lineas = leerLineas(archivo);
        boolean encontrado = lineas.removeIf(l -> l.startsWith(id + "|"));
        if (encontrado) {
            reescribirArchivo(archivo, lineas);
        }
        return encontrado;
    }

    /** Busca una línea específica por id. Devuelve null si no existe. */
    public static String buscarPorId(String archivo, String id) {
        for (String linea : leerLineas(archivo)) {
            if (linea.startsWith(id + "|")) {
                return linea;
            }
        }
        return null;
    }

    private static void reescribirArchivo(String archivo, List<String> lineas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            for (String l : lineas) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudo actualizar el archivo: " + e.getMessage());
        }
    }
}
