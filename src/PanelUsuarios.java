import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelUsuarios extends JPanel {

    private final JTextField campoId = new JTextField(8);
    private final JTextField campoNombre = new JTextField(15);
    private final JTextField campoUsuario = new JTextField(12);
    private final JPasswordField campoClave = new JPasswordField(12);
    private final JComboBox<String> campoRol = new JComboBox<>(new String[]{"ADMIN", "ADMISION", "TRIAJE", "MEDICO"});
    private final JLabel etiquetaEstado = new JLabel(" ");
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Usuario", "Clave protegida", "Rol"}, 0);

    public PanelUsuarios() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(construirFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modeloTabla)), BorderLayout.CENTER);
        actualizarTabla();
        Tema.aplicar(this);
    }

    private JPanel construirFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("CRUD de Usuarios"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        campo(panel, c, 0, "ID:", campoId);
        campo(panel, c, 1, "Nombre:", campoNombre);
        campo(panel, c, 2, "Usuario:", campoUsuario);
        campo(panel, c, 3, "Contraseña:", campoClave);
        campo(panel, c, 4, "Rol:", campoRol);

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
            Usuario.crear(campoId.getText().trim(), campoNombre.getText().trim(),
                    campoUsuario.getText().trim(), new String(campoClave.getPassword()),
                    (String) campoRol.getSelectedItem());
            exito("Usuario registrado correctamente."); limpiarCampos(); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void modificar() {
        try {
            Usuario.modificar(campoId.getText().trim(), campoNombre.getText().trim(),
                    campoUsuario.getText().trim(), new String(campoClave.getPassword()),
                    (String) campoRol.getSelectedItem());
            exito("Usuario modificado correctamente."); actualizarTabla();
        } catch (OperacionInvalidaException ex) { error(ex.getMessage()); }
    }

    private void eliminar() {
        if (Usuario.eliminar(campoId.getText().trim())) { exito("Usuario eliminado."); limpiarCampos(); actualizarTabla(); }
        else error("No se encontró un usuario con ese ID.");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (String[] fila : Usuario.listar()) modeloTabla.addRow(fila);
    }

    private void limpiarCampos() {
        campoId.setText(""); campoNombre.setText(""); campoUsuario.setText("");
        campoClave.setText(""); campoRol.setSelectedIndex(0);
    }

    private void exito(String msg) { etiquetaEstado.setForeground(new Color(0, 110, 0)); etiquetaEstado.setText(msg); }
    private void error(String msg) { etiquetaEstado.setForeground(Color.RED); etiquetaEstado.setText(msg); }
}
