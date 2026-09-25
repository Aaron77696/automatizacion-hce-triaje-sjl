public class PersonalMedico {

    private static final String ARCHIVO = "personal_medico.txt";

    private String id;
    private String nombre;
    private String cargo; // "MEDICO" o "ENFERMERA"
    private String especialidad;

    public PersonalMedico(String id, String nombre, String cargo, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.especialidad = especialidad;
    }

    public void crear() throws OperacionInvalidaException {
        if (nombre == null || nombre.isBlank()) {
            throw new OperacionInvalidaException("El nombre del personal médico no puede estar vacío.");
        }
        if (ArchivoUtil.buscarPorId(ARCHIVO, id) != null) {
            throw new OperacionInvalidaException("Ya existe personal médico con el id " + id);
        }
        ArchivoUtil.agregarLinea(ARCHIVO, id + "|" + nombre + "|" + cargo + "|" + especialidad);
    }

    public static String buscar(String id) {
        return (String) ArchivoUtil.buscarPorId(ARCHIVO, id);
    }

    public static boolean modificar(String id, String nuevoNombre, String nuevoCargo, String nuevaEspecialidad)
            throws OperacionInvalidaException {
        if (ArchivoUtil.buscarPorId(ARCHIVO, id) == null) {
            throw new OperacionInvalidaException("No existe personal médico con id " + id);
        }
        return ArchivoUtil.actualizarLinea(ARCHIVO, id, id + "|" + nuevoNombre + "|" + nuevoCargo + "|" + nuevaEspecialidad);
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }

    private static class OperacionInvalidaException extends Exception {

        public OperacionInvalidaException() {
        }

        private OperacionInvalidaException(String el_nombre_del_personal_médico_no_puede_es) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }

    private static class ArchivoUtil {

        private static Object buscarPorId(String ARCHIVO, String id) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static void agregarLinea(String ARCHIVO, String string) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static boolean actualizarLinea(String ARCHIVO, String id, String string) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static boolean eliminarLinea(String ARCHIVO, String id) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public ArchivoUtil() {
        }
    }
}
