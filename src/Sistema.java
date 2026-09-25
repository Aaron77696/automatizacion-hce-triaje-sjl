/**
 * Sistema: punto de entrada con el menú principal (login primero,
 * luego acceso a los módulos CRUD indicados por el docente).
 */
public class Sistema {

    public static void main(String[] args) {
        System.out.println("===== SISTEMA HCE - TRIAJE SJL (versión CRUD en archivos) =====");

        boolean acceso = Usuario.iniciarSesion("admin", "admin123");
        if (!acceso) {
            System.out.println("Acceso denegado. Cierre del sistema.");
            return;
        }
        System.out.println("Acceso concedido. Bienvenido al sistema.\n");
        System.out.println("Menú disponible: 1) Usuarios 2) Personal médico 3) Medicamentos 4) Pacientes 5) Citas");
    }
}
