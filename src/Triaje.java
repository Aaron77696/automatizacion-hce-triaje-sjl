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

    /**
     * RF-04: registra funciones vitales con validación estricta.
     * Manejo de excepciones: lanza DatosInvalidosException ante valores
     * fisiológicamente imposibles, evitando clasificar con datos corruptos.
     */
    public void registrarSignosVitales(double presionArterial, double temperatura, double frecuenciaCardiaca)
            throws DatosInvalidosException {
        if (presionArterial < 40 || presionArterial > 260) {
            throw new DatosInvalidosException("Presión arterial fuera de rango físicamente posible: " + presionArterial);
        }
        if (temperatura < 25 || temperatura > 45) {
            throw new DatosInvalidosException("Temperatura corporal fuera de rango posible: " + temperatura);
        }
        if (frecuenciaCardiaca < 20 || frecuenciaCardiaca > 250) {
            throw new DatosInvalidosException("Frecuencia cardíaca fuera de rango posible: " + frecuenciaCardiaca);
        }
        this.presionArterial = presionArterial;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    /** RF-05: clasificación por reglas clínicas (paradigma estructurado). */
    public int clasificarPrioridad() {
        if (presionArterial < 70 || frecuenciaCardiaca > 150 || temperatura > 40) {
            prioridad = 1; // crítico
        } else if (presionArterial < 90 || frecuenciaCardiaca > 120 || temperatura > 39) {
            prioridad = 2; // urgente
        } else if (temperatura > 38) {
            prioridad = 3; // moderado
        } else if (frecuenciaCardiaca > 100) {
            prioridad = 4; // leve
        } else {
            prioridad = 5; // sin urgencia
        }
        return prioridad;
    }

    /** RF-06: deriva al paciente según la prioridad calculada. */
    public String derivarPaciente() {
        return switch (prioridad) {
            case 1 -> "Shock Trauma / Atención inmediata";
            case 2 -> "Consultorio de Urgencias";
            case 3 -> "Consultorio de Medicina General (prioritario)";
            case 4 -> "Consultorio de Medicina General";
            default -> "Sala de espera - Consulta programada";
        };
    }

    public String generarResumen() {
        return "Triaje#" + idTriaje + " Paciente#" + idPaciente + " Prioridad=" + prioridad + " -> " + derivarPaciente();
    }

    public int getPrioridad() { return prioridad; }
    public int getIdPaciente() { return idPaciente; }
}