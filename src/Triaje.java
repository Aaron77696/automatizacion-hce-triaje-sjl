public class Triaje {

    private final int idTriaje;
    private final int idPaciente;
    private double presionArterial;
    private double temperatura;
    private double frecuenciaCardiaca;
    private int prioridad; // 1 (crítico) a 5 (leve)

    public Triaje(int idTriaje, int idPaciente) {
        this.idTriaje = idTriaje;
        this.idPaciente = idPaciente;
    }

    // Registra las funciones vitales del paciente, validando rangos posibles.
    public void registrarSignosVitales(double presionArterial, double temperatura, double frecuenciaCardiaca)
            throws DatosInvalidosException {
        if (presionArterial < 40 || presionArterial > 260) {
            throw new DatosInvalidosException("Presión arterial fuera de rango: " + presionArterial);
        }
        if (temperatura < 25 || temperatura > 45) {
            throw new DatosInvalidosException("Temperatura fuera de rango: " + temperatura);
        }
        if (frecuenciaCardiaca < 20 || frecuenciaCardiaca > 250) {
            throw new DatosInvalidosException("Frecuencia cardíaca fuera de rango: " + frecuenciaCardiaca);
        }
        this.presionArterial = presionArterial;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    // Clasifica la prioridad del paciente según sus signos vitales.
    public int clasificarPrioridad() {
        if (frecuenciaCardiaca > 130 || temperatura > 39.5) {
            prioridad = 1;
        } else if (frecuenciaCardiaca > 110 || temperatura > 38.5) {
            prioridad = 2;
        } else if (temperatura > 38) {
            prioridad = 3;
        } else if (frecuenciaCardiaca > 100) {
            prioridad = 4;
        } else {
            prioridad = 5;
        }
        return prioridad;
    }

    public int getPrioridad() { return prioridad; }
    public int getIdPaciente() { return idPaciente; }
}