import java.io.File;

/** Pruebas automatizadas de la capa de datos (sin interfaz gráfica). */
public class PruebasModelo {

    static int pasadas = 0, falladas = 0;

    public static void main(String[] args) {
        limpiar();
        System.out.println("===== PRUEBAS DE LA CAPA DE DATOS =====\n");

        prueba1_UsuarioYLogin();
        prueba2_PersonalMedico();
        prueba3_Medicamento();
        prueba4_PacienteDniProtegido();
        prueba5_CitaConValidaciones();
        prueba6_FuncionesOrdenSuperior();
        prueba7_MenusDesplegables();

        System.out.println("\n===== RESUMEN =====");
        System.out.println("Pasadas: " + pasadas + " | Falladas: " + falladas);
    }

    static void verificar(String nombre, boolean cond, String detalle) {
        if (cond) { pasadas++; System.out.println("[PASÓ] " + nombre + " -> " + detalle); }
        else { falladas++; System.out.println("[FALLÓ] " + nombre + " -> " + detalle); }
    }

    static void prueba1_UsuarioYLogin() {
        try {
            Usuario.crear("1", "Administrador", "admin", "admin123", "ADMIN");
            boolean loginOk = Usuario.iniciarSesion("admin", "admin123");
            boolean loginMal = Usuario.iniciarSesion("admin", "mala");
            verificar("UsuarioYLogin", loginOk && !loginMal, "login correcto=" + loginOk + ", login malo=" + loginMal);
        } catch (OperacionInvalidaException e) {
            verificar("UsuarioYLogin", false, e.getMessage());
        }
    }

    static void prueba2_PersonalMedico() {
        try {
            PersonalMedico.crear("1", "Dra. Fernández", "70011223", "DOCTOR", "Medicina General", "999111222");
            boolean esDoctor = PersonalMedico.existeDoctor("1");
            verificar("PersonalMedico", esDoctor, "existeDoctor=" + esDoctor);
        } catch (OperacionInvalidaException e) {
            verificar("PersonalMedico", false, e.getMessage());
        }
    }

    static void prueba3_Medicamento() {
        try {
            Medicamento.crear("1", "Amoxicilina", "250mg", "50", "Antibiótico");
            boolean rechazaNegativo = false;
            try {
                Medicamento.crear("2", "Ibuprofeno", "400mg", "-5", "Antiinflamatorio");
            } catch (OperacionInvalidaException e) {
                rechazaNegativo = true;
            }
            verificar("Medicamento", rechazaNegativo, "rechazó stock negativo=" + rechazaNegativo);
        } catch (OperacionInvalidaException e) {
            verificar("Medicamento", false, e.getMessage());
        }
    }

    static void prueba4_PacienteDniProtegido() {
        try {
            Paciente.crear("1", "Luis", "Torres", "70099887", "15/03/1990", "999888777", "Av. Siempre Viva 123");
            String encontrado = Paciente.buscar("70099887");
            boolean protegido = encontrado != null && !encontrado.contains("70099887") && encontrado.contains("700****87");
            verificar("PacienteDniProtegido", protegido, "buscar -> " + encontrado);
        } catch (OperacionInvalidaException e) {
            verificar("PacienteDniProtegido", false, e.getMessage());
        }
    }

    static void prueba5_CitaConValidaciones() {
        try {
            Cita.crear("1", "70099887", "1", "1", "25/09/2026 10:00", "Control general", "II");
            boolean rechazaSinPaciente = false;
            try {
                Cita.crear("2", "99999999", "1", "1", "25/09/2026 11:00", "Consulta", "I");
            } catch (OperacionInvalidaException e) {
                rechazaSinPaciente = true;
            }
            verificar("CitaConValidaciones", rechazaSinPaciente, "rechazó cita con paciente inexistente=" + rechazaSinPaciente);
        } catch (OperacionInvalidaException e) {
            verificar("CitaConValidaciones", false, e.getMessage());
        }
    }

    static void prueba6_FuncionesOrdenSuperior() {
        try {
            Cita.crear("3", "70099887", "1", "1", "26/09/2026 09:00", "Chequeo leve", "V");
            var criticos = Cita.filtrarCasosCriticos();
            var resumenes = Cita.generarResumenes();
            verificar("FuncionesOrdenSuperior",
                    criticos.size() == 1 && resumenes.size() == 2,
                    "citas críticas (filter)=" + criticos.size() + "/2, resúmenes (map)=" + resumenes);
        } catch (OperacionInvalidaException e) {
            verificar("FuncionesOrdenSuperior", false, e.getMessage());
        }
    }

    static void prueba7_MenusDesplegables() {
        try {
            PersonalMedico.crear("2", "Enf. Rojas", "70055566", "ENFERMERA", "Triaje", "999000111");
            var doctores = PersonalMedico.listarDoctoresParaMenu();
            var medicinas = Medicamento.listarParaMenu();
            boolean soloDoctores = doctores.size() == 1 && doctores.get(0).startsWith("1 - Dra. Fernández");
            boolean hayMedicinas = medicinas.size() == 1 && medicinas.get(0).startsWith("1 - Amoxicilina");
            verificar("MenusDesplegables", soloDoctores && hayMedicinas,
                    "doctores=" + doctores + " (sin enfermeras), medicamentos=" + medicinas);
        } catch (OperacionInvalidaException e) {
            verificar("MenusDesplegables", false, e.getMessage());
        }
    }

    static void limpiar() {
        String[] archivos = {"usuarios.txt", "personal_medico.txt", "medicamentos.txt", "pacientes.txt", "citas.txt"};
        for (String a : archivos) new File(ArchivoUtil.ruta(a)).delete();
    }
}
