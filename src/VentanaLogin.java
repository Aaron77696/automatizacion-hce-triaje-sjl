import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private final JTextField campoUsuario = new JTextField(15);
    private final JPasswordField campoClave = new JPasswordField(15);
    private final JLabel etiquetaEstado = new JLabel(" ");
    private int intentos = 0;

    public VentanaLogin() {
        setTitle("Inicio de sesión - Sistema HCE");
        setSize(380, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Sistema de Historias Clínicas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(Tema.AZUL_OSCURO);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(titulo, c);

        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        panel.add(campoUsuario, c);

        c.gridx = 0; c.gridy = 2;
        panel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1;
        panel.add(campoClave, c);

        JButton botonEntrar = new JButton("Iniciar sesión");
        botonEntrar.addActionListener(e -> intentarIngreso());
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        panel.add(botonEntrar, c);

        etiquetaEstado.setForeground(Color.RED);
        c.gridy = 4;
        panel.add(etiquetaEstado, c);

        Tema.aplicar(panel);
        getRootPane().setDefaultButton(botonEntrar);
        setContentPane(panel);
    }

    private void intentarIngreso() {
        String usuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword());

        if (Usuario.iniciarSesion(usuario, clave)) {
            dispose();
            SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
            return;
        }

        intentos++;
        int restantes = Usuario.intentosMaximos() - intentos;
        if (restantes > 0) {
            etiquetaEstado.setText("Usuario o contraseña incorrectos. Intentos restantes: " + restantes);
        } else {
            etiquetaEstado.setText("Límite de intentos alcanzado. Cerrando...");
            campoUsuario.setEnabled(false);
            campoClave.setEnabled(false);
            Timer temporizador = new Timer(1500, e -> System.exit(0));
            temporizador.setRepeats(false);
            temporizador.start();
        }
        campoClave.setText("");
    }
}
