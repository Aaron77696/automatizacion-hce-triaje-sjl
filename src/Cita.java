import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Cita {

    private static final String ARCHIVO = ArchivoUtil.ruta("citas.txt");
    private static final List<String> PRIORIDADES_VALIDAS = Arrays.asList("I", "II", "III", "IV", "V");

    public static void crear(String id, String dniPaciente, String idDoctor, String idMedicamento,
                              String fechaHora, String motivo, String prioridad) throws OperacionInvalidaException {
        if (id == null || id.isBlank() || dniPaciente == null || dniPaciente.isBlank()) {
            throw new OperacionInvalidaException("ID de cita y DNI del paciente son obligatorios.");
        }
        if (ArchivoUtil.existeValor(ARCHIVO, 0, id)) {
            throw new OperacionInvalidaException("Ese ID de cita ya existe.");
        }
        if (!Paciente.existeConDni(dniPaciente)) {
            throw new OperacionInvalidaException("No existe un paciente con ese DNI. Regístrelo primero.");
        }
        String idDoctorFinal = PersonalMedico.resolverDoctor(idDoctor);
        if (idDoctorFinal == null) {
            throw new OperacionInvalidaException("No existe un doctor con ese ID o nombre. Regístrelo primero.");
        }
        String idMedicamentoFinal = Medicamento.resolverId(idMedicamento);
        if (idMedicamentoFinal == null) {
            throw new OperacionInvalidaException("No existe un medicamento con ese ID o nombre. Regístrelo primero.");
        }
        String prioridadFinal = prioridad == null ? "" : prioridad.toUpperCase();
        if (!PRIORIDADES_VALIDAS.contains(prioridadFinal)) {
            throw new OperacionInvalidaException("La prioridad debe ser I, II, III, IV o V.");
        }
        ArchivoUtil.agregarLinea(ARCHIVO,
                ArchivoUtil.unir(id, dniPaciente, idDoctorFinal, idMedicamentoFinal, fechaHora, motivo, prioridadFinal));
    }

    public static String buscar(String id) {
        return ArchivoUtil.buscarPorId(ARCHIVO, id);
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }

    public static java.util.List<String[]> listar() {
        return ArchivoUtil.leerComoFilas(ARCHIVO);
    }

    /** Genera un ID aleatorio con formato Letra-6dígitos (ej. K482913). */
    public static String nuevoId() {
        return GeneradorId.nuevo(ARCHIVO);
    }


    public static List<String[]> filtrarCasosCriticos() {
        return listar().stream()
                .filter(fila -> fila.length == 7 && (fila[6].equals("I") || fila[6].equals("II")))
                .collect(Collectors.toList());
    }


    public static List<String> generarResumenes() {
        return listar().stream()
                .map(f -> "Cita " + f[0] + " | Paciente DNI " + f[1] + " | Prioridad " + f[6])
                .collect(Collectors.toList());
    }
}
