import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PanelCitas extends JPanel {

    private static final String FORMATO_FECHA = "dd/MM/yyyy HH:mm";

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoDniPaciente = new JTextField(10);
    private final JTextField campoIdDoctor = new JTextField(8);
    private final JTextField campoIdMedicamento = new JTextField(8);
    private final JSpinner campoFechaHora = crearSelectorFecha();
    private final JTextField campoMotivo = new JTextField(16);
    private final JComboBox<String> campoPrioridad = new JComboBox<>(new String[]{"I", "II", "III", "IV", "V"});
    private final JLabel etiquetaEstado = new JLabel(" ");
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "DNI Paciente", "ID Doctor", "ID Medicamento", "Fecha/Hora", "Motivo", "Prioridad"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    public PanelCitas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        campoId.setEditable(false);
        campoId.setText(Cita.nuevoId());
        add(construirFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Al hacer clic en una fila, se carga su ID (para poder eliminar)
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila >= 0) {
                campoId.setText((String) modeloTabla.getValueAt(fila, 0));
            }
        });

        actualizarTabla();
        Tema.aplicar(this);
    }

    private static JSpinner crearSelectorFecha() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE));
        spinner.setEditor(new JSpinner.DateEditor(spinner, FORMATO_FECHA));
        return spinner;
    }

    private JPanel construirFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registro de Citas de Pacientes"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        campo(panel, c, 0, "ID cita:", campoId);
        campo(panel, c, 1, "DNI paciente:", campoDniPaciente);
        campo(panel, c, 2, "Doctor (ID o nombre):", campoIdDoctor);
        campo(panel, c, 3, "Medicamento (ID o nombre):", campoIdMedicamento);
        campo(panel, c, 4, "Fecha/hora:", campoFechaHora);
        campo(panel, c, 5, "Motivo:", campoMotivo);
        campo(panel, c, 6, "Prioridad triaje:", campoPrioridad);

        JPanel botones = new JPanel();
        JButton registrar = new JButton("Registrar cita");
        JButton eliminar = new JButton("Eliminar");
        JButton verCriticas = new JButton("Ver solo críticas (I/II)");
        JButton limpiar = new JButton("Limpiar / ver todas");
        registrar.addActionListener(e -> registrar());
        eliminar.addActionListener(e -> eliminar());
        verCriticas.addActionListener(e -> mostrarCriticas());
        limpiar.addActionListener(e -> { limpiarCampos(); actualizarTabla(); });
        botones.add(registrar); botones.add(eliminar); botones.add(verCriticas); botones.add(limpiar);

        c.gridx = 0; c.gridy = 7; c.gridwidth = 2;
        panel.add(botones, c);
        c.gridy = 8;
        panel.add(etiquetaEstado, c);
        return panel;
    }

    private void campo(JPanel panel, GridBagConstraints c, int fila, String etiqueta, JComponent componente) {
        c.gridx = 0; c.gridy = fila; c.gridwidth = 1;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        panel.add(componente, c);
    }

    private void registrar() {
        try {
            String fechaHora = new SimpleDateFormat(FORMATO_FECHA).format((Date) campoFechaHora.getValue());
            Cita.crear(campoId.getText().trim(), campoDniPaciente.getText().trim(), campoIdDoctor.getText().trim(),
                    campoIdMedicamento.getText().trim(), fechaHora, campoMotivo.getText().trim(),
                    (String) campoPrioridad.getSelectedItem());
            exito("Cita registrada correctamente."); limpiarCampos(); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void eliminar() {
        if (Cita.eliminar(campoId.getText().trim())) { exito("Cita eliminada."); limpiarCampos(); actualizarTabla(); }
        else error("Seleccione una cita de la tabla para eliminarla.");
    }

    /** Usa la función de orden superior filter() de la clase Cita. */
    private void mostrarCriticas() {
        modeloTabla.setRowCount(0);
        for (String[] fila : Cita.filtrarCasosCriticos()) modeloTabla.addRow(fila);
        exito("Mostrando solo citas con prioridad I o II.");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (String[] fila : Cita.listar()) modeloTabla.addRow(fila);
    }

    private void limpiarCampos() {
        campoId.setText(Cita.nuevoId());
        campoDniPaciente.setText(""); campoIdDoctor.setText("");
        campoIdMedicamento.setText(""); campoMotivo.setText("");
        campoFechaHora.setValue(new Date());
        campoPrioridad.setSelectedIndex(0);
    }

    private void exito(String msg) { etiquetaEstado.setForeground(new Color(0, 110, 0)); etiquetaEstado.setText(msg); }
    private void error(String msg) { etiquetaEstado.setForeground(Color.RED); etiquetaEstado.setText(msg); }
}
