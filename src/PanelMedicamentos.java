import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelMedicamentos extends JPanel {

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoNombre = new JTextField(15);
    private final JTextField campoDosis = new JTextField(10);
    private final JTextField campoStock = new JTextField(6);
    private final JTextField campoDescripcion = new JTextField(20);
    private final JLabel etiquetaEstado = new JLabel(" ");
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Código", "Nombre", "Dosis", "Stock", "Descripción"}, 0);

    private final JTable tabla = new JTable(modeloTabla);

    public PanelMedicamentos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        campoId.setEditable(false);
        campoId.setText(Medicamento.nuevoId());
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
        panel.setBorder(BorderFactory.createTitledBorder("CRUD de Medicamentos"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        campo(panel, c, 0, "Código:", campoId);
        campo(panel, c, 1, "Nombre:", campoNombre);
        campo(panel, c, 2, "Dosis:", campoDosis);
        campo(panel, c, 3, "Stock:", campoStock);
        campo(panel, c, 4, "Descripción:", campoDescripcion);

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

        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        panel.add(botones, c);
        c.gridy = 6;
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
            Medicamento.crear(campoId.getText().trim(), campoNombre.getText().trim(), campoDosis.getText().trim(),
                    campoStock.getText().trim(), campoDescripcion.getText().trim());
            exito("Medicamento registrado."); limpiarCampos(); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void modificar() {
        try {
            Medicamento.modificar(campoId.getText().trim(), campoNombre.getText().trim(), campoDosis.getText().trim(),
                    campoStock.getText().trim(), campoDescripcion.getText().trim());
            exito("Medicamento modificado."); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void eliminar() {
        if (Medicamento.eliminar(campoId.getText().trim())) { exito("Medicamento eliminado."); limpiarCampos(); actualizarTabla(); }
        else error("No se encontró el medicamento.");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (String[] fila : Medicamento.listar()) modeloTabla.addRow(fila);
    }

    private void limpiarCampos() {
        campoId.setText(Medicamento.nuevoId()); campoNombre.setText(""); campoDosis.setText("");
        campoStock.setText(""); campoDescripcion.setText("");
    }

    private void cargarFila(int f) {
        campoId.setText(valor(f, 0));
        campoNombre.setText(valor(f, 1));
        campoDosis.setText(valor(f, 2));
        campoStock.setText(valor(f, 3));
        campoDescripcion.setText(valor(f, 4));
    }

    private String valor(int f, int c) { return String.valueOf(modeloTabla.getValueAt(f, c)); }

    private void exito(String msg) { etiquetaEstado.setForeground(new Color(0, 110, 0)); etiquetaEstado.setText(msg); }
    private void error(String msg) { etiquetaEstado.setForeground(Color.RED); etiquetaEstado.setText(msg); }
}
