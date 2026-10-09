import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPacientes extends JPanel {

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoNombre = new JTextField(14);
    private final JTextField campoApellido = new JTextField(14);
    private final JTextField campoDni = new JTextField(10);
    private final JTextField campoFecha = new JTextField(10);
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
        campo(panel, c, 4, "F. Nacimiento (dd/mm/aaaa):", campoFecha);
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

    private void campo(JPanel panel, GridBagConstraints c, int fila, String etiqueta, JComponent componente) {
        c.gridx = 0; c.gridy = fila; c.gridwidth = 1;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        panel.add(componente, c);
    }

    private void registrar() {
        try {
            Paciente.crear(campoId.getText().trim(), campoNombre.getText().trim(), campoApellido.getText().trim(),
                    campoDni.getText().trim(), campoFecha.getText().trim(), campoTelefono.getText().trim(), campoDireccion.getText().trim());
            exito("Paciente registrado."); limpiarCampos(); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void modificar() {
        try {
            Paciente.modificar(campoId.getText().trim(), campoNombre.getText().trim(), campoApellido.getText().trim(),
                    campoFecha.getText().trim(), campoTelefono.getText().trim(), campoDireccion.getText().trim());
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
        campoFecha.setText(""); campoTelefono.setText(""); campoDireccion.setText("");
    }

    private void cargarFila(int f) {
        campoId.setText(valor(f, 0));
        campoNombre.setText(valor(f, 1));
        campoApellido.setText(valor(f, 2));
        campoDni.setText(""); // la tabla solo tiene el DNI protegido; al modificar el DNI no cambia
        campoFecha.setText(valor(f, 4));
        campoTelefono.setText(valor(f, 5));
        campoDireccion.setText(valor(f, 6));
    }

    private String valor(int f, int c) { return String.valueOf(modeloTabla.getValueAt(f, c)); }

    private void exito(String msg) { etiquetaEstado.setForeground(new Color(0, 110, 0)); etiquetaEstado.setText(msg); }
    private void error(String msg) { etiquetaEstado.setForeground(Color.RED); etiquetaEstado.setText(msg); }
}
