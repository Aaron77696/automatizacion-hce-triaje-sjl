import java.util.List;

/**
 * Pruebas automatizadas del núcleo del sistema.
 * Cada prueba imprime PASÓ / FALLÓ según el resultado obtenido.
 */
public class PruebasSistema {

    static int pasadas = 0;
    static int falladas = 0;

    public static void main(String[] args) {
        System.out.println("===== EJECUCIÓN DE PRUEBAS AUTOMATIZADAS =====\n");

        prueba1_ClasificacionPrioridadCritica();
        prueba2_ExcepcionDatosInvalidos();
        prueba3_ProteccionDatosPersonales();
        prueba4_FuncionesOrdenSuperior();

        System.out.println("\n===== RESUMEN =====");
        System.out.println("Pruebas pasadas: " + pasadas);
        System.out.println("Pruebas falladas: " + falladas);
    }

    static void verificar(String nombrePrueba, boolean condicion, String detalle) {
        if (condicion) {
            pasadas++;
            System.out.println("[PASÓ] " + nombrePrueba + " -> " + detalle);
        } else {
            falladas++;
            System.out.println("[FALLÓ] " + nombrePrueba + " -> " + detalle);
        }
    }

    // Prueba 1: la clasificación asigna prioridad crítica correctamente
    static void prueba1_ClasificacionPrioridadCritica() {
        Triaje t = new Triaje(1, 101);
        try {
            t.registrarSignosVitales(60, 41.5, 160); // valores críticos
            int prioridad = t.clasificarPrioridad();
            verificar("prueba1_ClasificacionPrioridadCritica", prioridad == 1,
                    "Prioridad calculada=" + prioridad + " (esperado=1), derivación=" + t.derivarPaciente());
        } catch (DatosInvalidosException e) {
            verificar("prueba1_ClasificacionPrioridadCritica", false, e.getMessage());
        }
    }

    // Prueba 2: el sistema rechaza signos vitales fisiológicamente imposibles
    static void prueba2_ExcepcionDatosInvalidos() {
        Triaje t = new Triaje(2, 102);
        boolean lanzoExcepcion = false;
        try {
            t.registrarSignosVitales(500, 37, 80); // presión imposible
        } catch (DatosInvalidosException e) {
            lanzoExcepcion = true;
        }
        verificar("prueba2_ExcepcionDatosInvalidos", lanzoExcepcion,
                "El sistema " + (lanzoExcepcion ? "rechazó correctamente" : "NO rechazó") + " datos inválidos");
    }

    // Prueba 3: los datos personales no se exponen en texto plano
    static void prueba3_ProteccionDatosPersonales() {
        Paciente p = new Paciente(1, "Ana", "Ramírez", "74812345", "1998-04-12");
        String consulta = p.consultarPaciente();
        boolean noExponeDocumentoCompleto = !consulta.contains("74812345");
        String hash = GestorSeguridad.hashearDato("claveSegura123");
        boolean hashNoEsTextoPlano = !hash.equals("claveSegura123");
        verificar("prueba3_ProteccionDatosPersonales",
                noExponeDocumentoCompleto && hashNoEsTextoPlano,
                "Consulta=\"" + consulta + "\" | hash generado=" + hash);
    }

    // Prueba 4: las funciones de orden superior filtran y transforman sin mutar la lista original
    static void prueba4_FuncionesOrdenSuperior() {
        try {
            Triaje t1 = new Triaje(3, 103);
            t1.registrarSignosVitales(65, 40.2, 155); // crítico
            t1.clasificarPrioridad();

            Triaje t2 = new Triaje(4, 104);
            t2.registrarSignosVitales(115, 37.0, 85); // leve
            t2.clasificarPrioridad();

            List<Triaje> cola = List.of(t1, t2);
            List<Triaje> criticos = ServicioTriaje.filtrarCasosCriticos(cola);
            List<String> mensajes = ServicioTriaje.generarMensajesDerivacion(cola);

            verificar("prueba4_FuncionesOrdenSuperior",
                    criticos.size() == 1 && cola.size() == 2 && mensajes.size() == 2,
                    "Casos críticos filtrados=" + criticos.size() + "/2, lista original intacta=" + cola.size()
                            + ", mensajes generados=" + mensajes);
        } catch (DatosInvalidosException e) {
            verificar("prueba4_FuncionesOrdenSuperior", false, e.getMessage());
        }
    }
}
