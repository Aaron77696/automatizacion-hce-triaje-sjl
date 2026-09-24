public class Paciente {

    private final int idPaciente;
    private String nombre;
    private String apellido;
    private final String documento; // dato sensible: nunca se expone completo
    private final String fechaNacimiento;

    public Paciente(int idPaciente, String nombre, String apellido, String documento, String fechaNacimiento) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
    }

    /** RF-01: registrar paciente a través de la conexión Singleton. */
    public boolean registrarPaciente() {
        ConexionBaseDatos bd = ConexionBaseDatos.obtenerInstancia();
        return bd.guardar("Paciente", nombre + " " + apellido + " (" + getDocumentoEnmascarado() + ")");
    }

    public void actualizarDatos(String nuevoNombre, String nuevoApellido) {
        if (nuevoNombre == null || nuevoNombre.isBlank() || nuevoApellido == null || nuevoApellido.isBlank()) {
            throw new IllegalArgumentException("Nombre y apellido no pueden estar vacíos.");
        }
        this.nombre = nuevoNombre;
        this.apellido = nuevoApellido;
    }

    /** RF-02: consulta sin exponer el documento completo (Ley N.° 29733). */
    public String consultarPaciente() {
        return "ID: " + idPaciente + " | " + nombre + " " + apellido
                + " | Doc: " + getDocumentoEnmascarado() + " | Nac: " + fechaNacimiento;
    }

    public String getDocumentoEnmascarado() {
        return GestorSeguridad.enmascararDocumento(documento);
    }

    public int getIdPaciente() { return idPaciente; }
    public String getNombre() { return nombre; }
}
