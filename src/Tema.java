import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class Tema {

    public static final Color FONDO = new Color(240, 246, 252);
    public static final Color AZUL = new Color(25, 118, 210);
    public static final Color AZUL_OSCURO = new Color(13, 59, 102);
    public static final Color VERDE = new Color(46, 150, 80);
    public static final Color ROJO = new Color(200, 55, 55);
    public static final Color NARANJA = new Color(230, 126, 34);
    public static final Color GRIS = new Color(110, 125, 140);
    public static final Color TEXTO = new Color(30, 40, 55);

    public static void aplicar(Container contenedor) {
        if (contenedor instanceof JPanel) contenedor.setBackground(FONDO);
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JButton) {
                estilizarBoton((JButton) c);
            } else if (c instanceof JTextComponent) {
                c.setBackground(Color.WHITE);
                c.setForeground(TEXTO);
                ((JComponent) c).setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(160, 185, 210), 1, true),
                        new EmptyBorder(3, 6, 3, 6)));
            } else if (c instanceof JComboBox) {
                c.setBackground(Color.WHITE);
                c.setForeground(TEXTO);
            } else if (c instanceof JScrollPane) {
                ((JScrollPane) c).getViewport().setBackground(Color.WHITE);
                ((JScrollPane) c).setBorder(new LineBorder(new Color(160, 185, 210), 1));
            } else if (c instanceof JTable) {
                estilizarTabla((JTable) c);
            } else if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                if (!l.getText().isBlank()) l.setForeground(TEXTO);
            } else if (c instanceof JPanel) {
                JPanel p = (JPanel) c;
                p.setBackground(FONDO);
                if (p.getBorder() instanceof TitledBorder) {
                    TitledBorder tb = (TitledBorder) p.getBorder();
                    tb.setTitleColor(AZUL_OSCURO);
                    tb.setTitleFont(tb.getTitleFont().deriveFont(Font.BOLD, 13f));
                }
            }
            if (c instanceof Container) aplicar((Container) c);
        }
    }

    public static void estilizarBoton(JButton b) {
        String t = b.getText().toLowerCase();
        Color fondo = AZUL;
        if (t.contains("eliminar") || t.contains("salir")) fondo = ROJO;
        else if (t.contains("registrar")) fondo = VERDE;
        else if (t.contains("modificar") || t.contains("críticas")) fondo = NARANJA;
        else if (t.contains("limpiar")) fondo = GRIS;
        pintar(b, fondo);
    }

    public static void botonMenu(JButton b) {
        pintar(b, new Color(30, 90, 150));
    }

    private static void pintar(JButton b, Color fondo) {
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static void estilizarTabla(JTable t) {
        t.setRowHeight(24);
        t.setGridColor(new Color(215, 225, 236));
        t.setSelectionBackground(new Color(190, 215, 245));
        t.setSelectionForeground(TEXTO);
        JTableHeader h = t.getTableHeader();
        h.setBackground(AZUL_OSCURO);
        h.setForeground(Color.WHITE);
        h.setFont(h.getFont().deriveFont(Font.BOLD));
    }
}
