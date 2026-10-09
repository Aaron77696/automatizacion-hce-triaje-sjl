import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPersonalMedico extends JPanel {

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoNombre = new JTextField(15);
    private final JTextField campoDni = new JTextField(10);
    private final JComboBox<String> campoTipo = new JComboBox<>(new String[]{"DOCTOR", "ENFERMERA", "OTRO"});
    private final JTextField campoEspecialidad = new JTextField(15);
    private final JTextField campoTelefono = new JTextField(12);
    private final JLabel etiquetaEstado = new JLabel(" ");
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "DNI", "Tipo", "Especialidad", "Teléfono"}, 0);

    private final JTable tabla = new JTable(modeloTabla);

    public PanelPersonalMedico() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        campoId.setEditable(false);
        campoId.setText(PersonalMedico.nuevoId());
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
        panel.setBorder(BorderFactory.createTitledBorder("CRUD de Personal Médico (Doctores / Enfermeras)"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        campo(panel, c, 0, "ID:", campoId);
        campo(panel, c, 1, "Nombre:", campoNombre);
        campo(panel, c, 2, "DNI:", campoDni);
        campo(panel, c, 3, "Tipo:", campoTipo);
        campo(panel, c, 4, "Especialidad:", campoEspecialidad);
        campo(panel, c, 5, "Teléfono:", campoTelefono);

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

        c.gridx = 0; c.gridy = 6; c.gridwidth = 2;
        panel.add(botones, c);
        c.gridy = 7;
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
            PersonalMedico.crear(campoId.getText().trim(), campoNombre.getText().trim(), campoDni.getText().trim(),
                    (String) campoTipo.getSelectedItem(), campoEspecialidad.getText().trim(), campoTelefono.getText().trim());
            exito("Personal médico registrado."); limpiarCampos(); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void modificar() {
        try {
            PersonalMedico.modificar(campoId.getText().trim(), campoNombre.getText().trim(), campoDni.getText().trim(),
                    (String) campoTipo.getSelectedItem(), campoEspecialidad.getText().trim(), campoTelefono.getText().trim());
            exito("Registro modificado."); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void eliminar() {
        if (PersonalMedico.eliminar(campoId.getText().trim())) { exito("Registro eliminado."); limpiarCampos(); actualizarTabla(); }
        else error("No se encontró el personal médico.");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (String[] fila : PersonalMedico.listar()) modeloTabla.addRow(fila);
    }

    private void limpiarCampos() {
        campoId.setText(PersonalMedico.nuevoId()); campoNombre.setText(""); campoDni.setText("");
        campoTipo.setSelectedIndex(0); campoEspecialidad.setText(""); campoTelefono.setText("");
    }

    private void cargarFila(int f) {
        campoId.setText(valor(f, 0));
        campoNombre.setText(valor(f, 1));
        campoDni.setText(valor(f, 2));
        campoTipo.setSelectedItem(valor(f, 3));
        campoEspecialidad.setText(valor(f, 4));
        campoTelefono.setText(valor(f, 5));
    }

    private String valor(int f, int c) { return String.valueOf(modeloTabla.getValueAt(f, c)); }

    private void exito(String msg) { etiquetaEstado.setForeground(new Color(0, 110, 0)); etiquetaEstado.setText(msg); }
    private void error(String msg) { etiquetaEstado.setForeground(Color.RED); etiquetaEstado.setText(msg); }
}
