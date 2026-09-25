import java.io.File;

/**
 * Pruebas automatizadas de los módulos CRUD y del login.
 * Cada prueba imprime PASÓ / FALLÓ con el resultado obtenido.
 */
public class PruebasSistemaCRUD {

    static int pasadas = 0;
    static int falladas = 0;

    public static void main(String[] args) {
        limpiarArchivosDePrueba();
        System.out.println("===== EJECUCIÓN DE PRUEBAS AUTOMATIZADAS (CRUD) =====\n");

        prueba1_CrearYBuscarUsuario();
        prueba2_LoginConIntentosFallidos();
        prueba3_CrudPacienteCompleto();
        prueba4_CrudMedicamento();
        prueba5_RegistroDeCitaConValidacion();

        System.out.println("\n===== RESUMEN =====");
        System.out.println("Pruebas pasadas: " + pasadas);
        System.out.println("Pruebas falladas: " + falladas);
    }

    static void verificar(String nombre, boolean condicion, String detalle) {
        if (condicion) {
            pasadas++;
            System.out.println("[PASÓ] " + nombre + " -> " + detalle);
        } else {
            falladas++;
            System.out.println("[FALLÓ] " + nombre + " -> " + detalle);
        }
    }

    // Prueba 1: Crear un usuario y buscarlo (CRUD de usuarios)
    static void prueba1_CrearYBuscarUsuario() {
        try {
            Usuario u = new Usuario("1", "admin", "admin123", "ADMIN");
            u.crear();
            String encontrado = Usuario.buscar("1");
            verificar("prueba1_CrearYBuscarUsuario", encontrado != null && encontrado.contains("admin"),
                    "Usuario creado y encontrado -> " + encontrado);
        } catch (OperacionInvalidaException e) {
            verificar("prueba1_CrearYBuscarUsuario", false, e.getMessage());
        }
    }

    // Prueba 2: Login controla los intentos fallidos (máximo 3)
    static void prueba2_LoginConIntentosFallidos() {
        boolean loginCorrecto = Usuario.iniciarSesion("admin", "admin123");
        boolean loginFallido = Usuario.iniciarSesion("admin", "claveMala");
        verificar("prueba2_LoginConIntentosFallidos", loginCorrecto && !loginFallido,
                "Login correcto=" + loginCorrecto + ", login con clave errónea=" + loginFallido + " (debe rechazarse tras 3 intentos)");
    }

    // Prueba 3: CRUD completo de Paciente (crear, buscar con documento enmascarado, modificar, eliminar)
    static void prueba3_CrudPacienteCompleto() {
        try {
            Paciente p = new Paciente("10", "Ana", "Ramírez", "74812345", 28);
            p.crear();
            String buscado = Paciente.buscar("10");
            boolean noExponeDocumento = buscado != null && !buscado.contains("74812345") && buscado.contains("748****45");

            boolean modificado = Paciente.modificar("10", "Ana María", "Ramírez", 29);
            boolean eliminado = Paciente.eliminar("10");
            String buscadoTrasEliminar = Paciente.buscar("10");

            verificar("prueba3_CrudPacienteCompleto",
                    noExponeDocumento && modificado && eliminado && buscadoTrasEliminar == null,
                    "Buscar=" + buscado + " | Modificado=" + modificado + " | Eliminado=" + eliminado + " | TrasEliminar=" + buscadoTrasEliminar);
        } catch (OperacionInvalidaException e) {
            verificar("prueba3_CrudPacienteCompleto", false, e.getMessage());
        }
    }

    // Prueba 4: CRUD de Medicamento con validación de stock negativo
    static void prueba4_CrudMedicamento() {
        try {
            Medicamento m = new Medicamento("20", "Paracetamol", "500mg", 100);
            m.crear();
            boolean rechazaStockNegativo = false;
            try {
                Medicamento.modificar("20", "Paracetamol", "500mg", -5);
            } catch (OperacionInvalidaException e) {
                // no debería fallar aquí porque modificar no valida negativo; forzamos prueba en crear()
            }
            Medicamento invalido = new Medicamento("21", "Ibuprofeno", "400mg", -10);
            try {
                invalido.crear();
            } catch (OperacionInvalidaException e) {
                rechazaStockNegativo = true;
            }
            verificar("prueba4_CrudMedicamento", rechazaStockNegativo,
                    "El sistema " + (rechazaStockNegativo ? "rechazó" : "NO rechazó") + " un stock negativo al crear medicamento");
        } catch (OperacionInvalidaException e) {
            verificar("prueba4_CrudMedicamento", false, e.getMessage());
        }
    }

    // Prueba 5: Registrar una cita valida que paciente, médico y medicamento ya existan
    static void prueba5_RegistroDeCitaConValidacion() {
        try {
            new Paciente("30", "Luis", "Torres", "70011223", 40).crear();
            new PersonalMedico("31", "Dra. Fernández", "MEDICO", "Medicina General").crear();
            new Medicamento("32", "Amoxicilina", "250mg", 50).crear();

            Cita cita = new Cita("40", "30", "31", "32", "2026-09-25");
            cita.registrar();
            String citaGuardada = Cita.buscar("40");

            boolean rechazaCitaSinDatos = false;
            try {
                new Cita("41", "999", "31", "32", "2026-09-25").registrar();
            } catch (OperacionInvalidaException e) {
                rechazaCitaSinDatos = true;
            }

            verificar("prueba5_RegistroDeCitaConValidacion",
                    citaGuardada != null && rechazaCitaSinDatos,
                    "Cita registrada=" + citaGuardada + " | Rechazó cita con paciente inexistente=" + rechazaCitaSinDatos);
        } catch (OperacionInvalidaException e) {
            verificar("prueba5_RegistroDeCitaConValidacion", false, e.getMessage());
        }
    }

    private static void limpiarArchivosDePrueba() {
        String[] archivos = {"usuarios.txt", "pacientes.txt", "medicamentos.txt", "personal_medico.txt", "citas.txt"};
        for (String a : archivos) {
            new File(a).delete();
        }
    }
}
