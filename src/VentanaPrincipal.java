import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelCentral = new JPanel(cardLayout);

    public VentanaPrincipal() {
        setTitle("Sistema de Historias Clínicas - Menú principal");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelCentral.add(new PanelInicio(), "INICIO");
        panelCentral.add(new PanelUsuarios(), "USUARIOS");
        panelCentral.add(new PanelPersonalMedico(), "PERSONAL");
        panelCentral.add(new PanelMedicamentos(), "MEDICAMENTOS");
        panelCentral.add(new PanelPacientes(), "PACIENTES");
        panelCentral.add(new PanelCitas(), "CITAS");

        setLayout(new BorderLayout());
        add(construirMenuLateral(), BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel construirMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        menu.setPreferredSize(new Dimension(180, 0));
        menu.setBackground(Tema.AZUL_OSCURO);

        menu.add(botonMenu("Inicio", "INICIO"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonMenu("Usuarios", "USUARIOS"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonMenu("Personal médico", "PERSONAL"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonMenu("Medicamentos", "MEDICAMENTOS"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonMenu("Pacientes", "PACIENTES"));
        menu.add(Box.createVerticalStrut(8));
        menu.add(botonMenu("Citas", "CITAS"));
        menu.add(Box.createVerticalGlue());

        JButton salir = new JButton("Salir");
        salir.addActionListener(e -> System.exit(0));
        Tema.estilizarBoton(salir);
        menu.add(salir);
        return menu;
    }

    private JButton botonMenu(String texto, String tarjeta) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(160, 30));
        boton.addActionListener(e -> cardLayout.show(panelCentral, tarjeta));
        Tema.botonMenu(boton);
        return boton;
    }

    static class PanelInicio extends JPanel {
        PanelInicio() {
            setLayout(new GridBagLayout());
            setBackground(Tema.FONDO);
            add(new JLabel(
                "<html><center><h2>Sistema de Historias Clínicas Electrónicas</h2>"
                + "Hospital San Juan de Lurigancho - Sede Central, Canto Grande<br><br>"
                + "Seleccione un módulo en el menú de la izquierda.</center></html>",
                SwingConstants.CENTER));
        }
    }
}
