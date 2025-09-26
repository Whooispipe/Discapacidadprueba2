import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diálogo sencillo para ver/editar servicios y notas de un beneficiario.
 * Permite agregar, editar y eliminar (SIA2.4).
 */
public class ServicesNotesDialog extends JDialog {
    private Beneficiario beneficiario;
    private DefaultListModel<String> modeloServicios;
    private DefaultListModel<String> modeloNotas;
    private JList<String> listaServicios;
    private JList<String> listaNotas;

    public ServicesNotesDialog(Frame owner, Beneficiario b) {
        super(owner, "Servicios y Notas - " + b.getNombre(), true);
        this.beneficiario = b;
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        modeloServicios = new DefaultListModel<>();
        modeloNotas = new DefaultListModel<>();
        listaServicios = new JList<>(modeloServicios);
        listaNotas = new JList<>(modeloNotas);
        actualizarListas();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearPanelServicios(), crearPanelNotas());
        split.setDividerLocation(300);
        add(split, BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        south.add(btnCerrar);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel crearPanelServicios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Servicios"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listaServicios), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Agregar");
        JButton btnEdit = new JButton("Editar");
        JButton btnDel = new JButton("Eliminar");

        btnAdd.addActionListener(e -> {
            JTextField tipo = new JTextField();
            JTextField desc = new JTextField();
            Object[] msg = {"Tipo:", tipo, "Descripción:", desc};
            if (JOptionPane.showConfirmDialog(this, msg, "Agregar Servicio", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                beneficiario.agregarServicioDeApoyo(tipo.getText().trim(), desc.getText().trim());
                actualizarListas();
            }
        });

        btnEdit.addActionListener(e -> {
            int idx = listaServicios.getSelectedIndex();
            if (idx < 0) return;
            ServiciodeApoyo s = beneficiario.getServiciosDeApoyo().get(idx);
            JTextField tipo = new JTextField(s.getTipoServicio());
            JTextField desc = new JTextField(s.getDescripcion());
            Object[] msg = {"Tipo:", tipo, "Descripción:", desc};
            if (JOptionPane.showConfirmDialog(this, msg, "Editar Servicio", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                beneficiario.editarServicio(idx, tipo.getText().trim(), desc.getText().trim());
                actualizarListas();
            }
        });

        btnDel.addActionListener(e -> {
            int idx = listaServicios.getSelectedIndex();
            if (idx < 0) return;
            if (JOptionPane.showConfirmDialog(this, "Eliminar servicio seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                beneficiario.eliminarServicio(idx);
                actualizarListas();
            }
        });

        btns.add(btnAdd);
        btns.add(btnEdit);
        btns.add(btnDel);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelNotas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Notas (Seguimiento)"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listaNotas), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Agregar");
        JButton btnEdit = new JButton("Editar");
        JButton btnDel = new JButton("Eliminar");

        btnAdd.addActionListener(e -> {
            String[] opciones = {"Positivo", "Negativo", "Otro"};
            String sel = (String) JOptionPane.showInputDialog(this, "Seleccione efecto:", "Agregar Nota", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (sel == null) return;
            if (sel.equals("Otro")) {
                String texto = JOptionPane.showInputDialog(this, "Ingrese texto de la nota:");
                if (texto != null && !texto.trim().isEmpty()) beneficiario.agregarNota(texto.trim());
            } else {
                beneficiario.agregarNota(sel);
            }
            actualizarListas();
        });

        btnEdit.addActionListener(e -> {
            int idx = listaNotas.getSelectedIndex();
            if (idx < 0) return;
            String actual = beneficiario.getSeguimientoImpacto().get(idx).getEfecto();
            String nuevo = JOptionPane.showInputDialog(this, "Editar nota:", actual);
            if (nuevo != null && !nuevo.trim().isEmpty()) {
                beneficiario.editarNota(idx, nuevo.trim());
                actualizarListas();
            }
        });

        btnDel.addActionListener(e -> {
            int idx = listaNotas.getSelectedIndex();
            if (idx < 0) return;
            if (JOptionPane.showConfirmDialog(this, "Eliminar nota seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                beneficiario.eliminarNota(idx);
                actualizarListas();
            }
        });

        btns.add(btnAdd);
        btns.add(btnEdit);
        btns.add(btnDel);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarListas() {
        modeloServicios.clear();
        modeloNotas.clear();
        for (ServiciodeApoyo s : beneficiario.getServiciosDeApoyo()) modeloServicios.addElement(s.toString());
        for (SeguimientoImpacto n : beneficiario.getSeguimientoImpacto()) modeloNotas.addElement(n.getEfecto());
    }
}