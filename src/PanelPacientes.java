import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;

public class PanelPacientes extends JPanel {

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoNombre = new JTextField(14);
    private final JTextField campoApellido = new JTextField(14);
    private final JTextField campoDni = new JTextField(10);
    private static final String[] MESES = {"Mes", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private final JComboBox<String> comboDia = new JComboBox<>(opcionesDias());
    private final JComboBox<String> comboMes = new JComboBox<>(MESES);
    private final JComboBox<String> comboAnio = new JComboBox<>(opcionesAnios());
    private final JTextField campoTelefono = new JTextField(12);
    private final JTextField campoDireccion = new JTextField(20);
    private final JLabel etiquetaEstado = new JLabel(" ");
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Apellido", "DNI (protegido)", "F. Nacimiento", "Teléfono", "Dirección"}, 0);

    private final JTable tabla = new JTable(modeloTabla);

    public PanelPacientes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        campoId.setEditable(false);
        campoId.setText(Paciente.nuevoId());
        add(construirFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Al hacer clic en una fila, se cargan sus datos en el formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila >= 0) cargarFila(fila);
        });
        actualizarTabla();
        Tema.aplicar(this);
    }

    private JPanel construirFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("CRUD de Pacientes"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        campo(panel, c, 0, "ID:", campoId);
        campo(panel, c, 1, "Nombres:", campoNombre);
        campo(panel, c, 2, "Apellidos:", campoApellido);
        campo(panel, c, 3, "DNI:", campoDni);
        campo(panel, c, 4, "F. Nacimiento:", construirSelectorFecha());
        campo(panel, c, 5, "Teléfono:", campoTelefono);
        campo(panel, c, 6, "Dirección:", campoDireccion);

        JPanel botones = new JPanel();
        JButton registrar = new JButton("Registrar");
        JButton modificar = new JButton("Modificar");
        JButton eliminar = new JButton("Eliminar");
        JButton limpiar = new JButton("Limpiar");
        registrar.addActionListener(e -> registrar());
        modificar.addActionListener(e -> modificar());
        eliminar.addActionListener(e -> eliminar());
        limpiar.addActionListener(e -> limpiarCampos());
        botones.add(registrar); botones.add(modificar); botones.add(eliminar); botones.add(limpiar);

        c.gridx = 0; c.gridy = 7; c.gridwidth = 2;
        panel.add(botones, c);
        c.gridy = 8;
        panel.add(etiquetaEstado, c);
        return panel;
    }

    private static String[] opcionesDias() {
        String[] dias = new String[32];
        dias[0] = "Día";
        for (int i = 1; i <= 31; i++) dias[i] = String.format("%02d", i);
        return dias;
    }

    private static String[] opcionesAnios() {
        int actual = LocalDate.now().getYear();
        String[] anios = new String[actual - 1900 + 2];
        anios[0] = "Año";
        for (int a = actual, i = 1; a >= 1900; a--, i++) anios[i] = String.valueOf(a);
        return anios;
    }

    private JPanel construirSelectorFecha() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.add(comboDia); p.add(comboMes); p.add(comboAnio);
        return p;
    }

    /** Devuelve la fecha como dd/MM/aaaa, "" si no se eligió nada, o lanza error si es inválida. */
    private String obtenerFecha() throws OperacionInvalidaException {
        int d = comboDia.getSelectedIndex(), m = comboMes.getSelectedIndex(), a = comboAnio.getSelectedIndex();
        if (d == 0 && m == 0 && a == 0) return "";
        if (d == 0 || m == 0 || a == 0) {
            throw new OperacionInvalidaException("Complete día, mes y año de nacimiento.");
        }
        int anio = Integer.parseInt((String) comboAnio.getSelectedItem());
        try {
            LocalDate fecha = LocalDate.of(anio, m, d);
            if (fecha.isAfter(LocalDate.now())) {
                throw new OperacionInvalidaException("La fecha de nacimiento no puede ser futura.");
            }
        } catch (DateTimeException e) {
            throw new OperacionInvalidaException("La fecha de nacimiento no es válida.");
        }
        return String.format("%02d/%02d/%04d", d, m, anio);
    }

    /** Coloca una fecha dd/MM/aaaa en los 3 desplegables (o los deja en blanco). */
    private void fijarFecha(String fecha) {
        String[] p = fecha == null ? new String[0] : fecha.split("/");
        if (p.length == 3) {
            try {
                comboDia.setSelectedIndex(Integer.parseInt(p[0].trim()));
                comboMes.setSelectedIndex(Integer.parseInt(p[1].trim()));
                comboAnio.setSelectedItem(p[2].trim());
                return;
            } catch (Exception e) { /* formato viejo o raro: se deja en blanco */ }
        }
        comboDia.setSelectedIndex(0); comboMes.setSelectedIndex(0); comboAnio.setSelectedIndex(0);
    }

    private void campo(JPanel panel, GridBagConstraints c, int fila, String etiqueta, JComponent componente) {
        c.gridx = 0; c.gridy = fila; c.gridwidth = 1;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        panel.add(componente, c);
    }

    private void registrar() {
        try {
            Paciente.crear(campoId.getText().trim(), campoNombre.getText().trim(), campoApellido.getText().trim(),
                    campoDni.getText().trim(), obtenerFecha(), campoTelefono.getText().trim(), campoDireccion.getText().trim());
            exito("Paciente registrado."); limpiarCampos(); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void modificar() {
        try {
            Paciente.modificar(campoId.getText().trim(), campoNombre.getText().trim(), campoApellido.getText().trim(),
                    obtenerFecha(), campoTelefono.getText().trim(), campoDireccion.getText().trim());
            exito("Paciente modificado."); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void eliminar() {
        if (Paciente.eliminar(campoId.getText().trim())) { exito("Paciente eliminado."); limpiarCampos(); actualizarTabla(); }
        else error("No se encontró el paciente.");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (String[] fila : Paciente.listar()) modeloTabla.addRow(fila);
    }

    private void limpiarCampos() {
        campoId.setText(Paciente.nuevoId()); campoNombre.setText(""); campoApellido.setText(""); campoDni.setText("");
        fijarFecha(""); campoTelefono.setText(""); campoDireccion.setText("");
    }

    private void cargarFila(int f) {
        campoId.setText(valor(f, 0));
        campoNombre.setText(valor(f, 1));
        campoApellido.setText(valor(f, 2));
        campoDni.setText(""); // la tabla solo tiene el DNI protegido; al modificar el DNI no cambia
        fijarFecha(valor(f, 4));
        campoTelefono.setText(valor(f, 5));
        campoDireccion.setText(valor(f, 6));
    }

    private String valor(int f, int c) { return String.valueOf(modeloTabla.getValueAt(f, c)); }

    private void exito(String msg) { etiquetaEstado.setForeground(new Color(0, 110, 0)); etiquetaEstado.setText(msg); }
    private void error(String msg) { etiquetaEstado.setForeground(Color.RED); etiquetaEstado.setText(msg); }
}
