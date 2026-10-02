/**
 * Cita: cubre "Registro de citas de pacientes". Debe buscar el doctor,
 * medicamento y paciente previamente guardados en sus propios archivos
 * antes de registrar la cita, tal como indicó el docente.
 * Persistencia en citas.txt (id|idPaciente|idMedico|idMedicamento|fecha).
 */
public class Cita {

    private static final String ARCHIVO = "citas.txt";

    private String id;
    private String idPaciente;
    private String idMedico;
    private String idMedicamento;
    private String fecha;

    public Cita(String id, String idPaciente, String idMedico, String idMedicamento, String fecha) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.idMedicamento = idMedicamento;
        this.fecha = fecha;
    }

    /**
     * Registra la cita solo si el paciente, el médico y el medicamento
     * ya existen guardados en sus respectivos "blocs de notas".
     */
    public void registrar() throws OperacionInvalidaException {
        if (Paciente.buscar(idPaciente) == null) {
            throw new OperacionInvalidaException("No se encontró el paciente con id " + idPaciente + ". Regístrelo primero.");
        }
        if (PersonalMedico.buscar(idMedico) == null) {
            throw new OperacionInvalidaException("No se encontró el médico con id " + idMedico + ". Regístrelo primero.");
        }
        if (Medicamento.buscar(idMedicamento) == null) {
            throw new OperacionInvalidaException("No se encontró el medicamento con id " + idMedicamento + ". Regístrelo primero.");
        }
        ArchivoUtil.agregarLinea(ARCHIVO, id + "|" + idPaciente + "|" + idMedico + "|" + idMedicamento + "|" + fecha);
    }

    public static String buscar(String id) {
        return ArchivoUtil.buscarPorId(ARCHIVO, id);
    }

    public static boolean eliminar(String id) {
        return ArchivoUtil.eliminarLinea(ARCHIVO, id);
    }
}
