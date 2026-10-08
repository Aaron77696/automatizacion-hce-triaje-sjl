import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class ArchivoUtil {

    private static final String CARPETA = "datos_hospital";

    public static String ruta(String nombreArchivo) {
        return CARPETA + File.separator + nombreArchivo;
    }

    public static void agregarLinea(String archivo, String linea) {
        try {
            Files.createDirectories(Paths.get(CARPETA));
            Files.write(
                Paths.get(archivo),
                (linea + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudo guardar el registro: " + e.getMessage());
        }
    }

    public static List<String> leerLineas(String archivo) {
        List<String> lineas = new ArrayList<>();
        try {
            if (!Files.exists(Paths.get(archivo))) return lineas;
            for (String l : Files.readAllLines(Paths.get(archivo), StandardCharsets.UTF_8)) {
                if (!l.isBlank()) lineas.add(l);
            }
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudo leer el archivo: " + e.getMessage());
        }
        return lineas;
    }

    public static List<String[]> leerComoFilas(String archivo) {
        List<String[]> filas = new ArrayList<>();
        for (String l : leerLineas(archivo)) {
            filas.add(l.split("\\|", -1));
        }
        return filas;
    }

    public static String buscarPorId(String archivo, String id) {
        for (String l : leerLineas(archivo)) {
            if (l.startsWith(id + "|")) return l;
        }
        return null;
    }

    public static boolean existeValor(String archivo, int indice, String valor) {
        for (String l : leerLineas(archivo)) {
            String[] d = l.split("\\|", -1);
            if (d.length > indice && d[indice].equalsIgnoreCase(valor)) return true;
        }
        return false;
    }

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
        if (encontrado) reescribir(archivo, lineas);
        return encontrado;
    }

    public static boolean eliminarLinea(String archivo, String id) {
        List<String> lineas = leerLineas(archivo);
        boolean eliminado = lineas.removeIf(l -> l.startsWith(id + "|"));
        if (eliminado) reescribir(archivo, lineas);
        return eliminado;
    }

    private static void reescribir(String archivo, List<String> lineas) {
        try {
            Files.write(Paths.get(archivo), lineas, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudo actualizar el archivo: " + e.getMessage());
        }
    }

    public static String unir(String... valores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) sb.append("|");
            sb.append(valores[i] == null ? "" : valores[i].replace("|", "/"));
        }
        return sb.toString();
    }
}
