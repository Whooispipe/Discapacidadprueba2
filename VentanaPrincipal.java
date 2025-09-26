import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Ventana principal sencilla con pestañas para Beneficiarios y Funcionarios.
 * Interfaz pensada para un estudiante 2° año: botones, listas y diálogos.
 *
 * - No usa la consola para interactuar.
 * - Maneja la persistencia mediante los repositorios al cerrar la ventana.
 */
public class VentanaPrincipal extends JFrame {
    private BeneficiarioRepository beneficiarioRepo;
    private FuncionarioRepository funcionarioRepo;

    private DefaultListModel<String> modeloBeneficiarios;
    private JList<String> listaBeneficiarios;

    private DefaultListModel<String> modeloFuncionarios;
    private JList<String> listaFuncionarios;

    public VentanaPrincipal() {
        super("Sistema Sencillo - Beneficiarios y Funcionarios");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        beneficiarioRepo = new BeneficiarioRepository();
        funcionarioRepo = new FuncionarioRepository();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Guardar datos antes de salir (manejo de excepciones) - SIA2.8
                try {
                    beneficiarioRepo.guardar();
                    funcionarioRepo.guardar();
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Datos guardados. Saliendo.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Error guardando datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                System.exit(0);
            }
        });

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Beneficiarios", crearPanelBeneficiarios());
        tabs.addTab("Funcionarios", crearPanelFuncionarios());

        getContentPane().add(tabs, BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton btnReporte = new JButton("Generar Reportes TXT");
        btnReporte.addActionListener(e -> {
            ReportGenerator.generarReporteBeneficiarios("reporte_beneficiarios.txt", beneficiarioRepo);
            ReportGenerator.generarReporteFuncionarios("reporte_funcionarios.txt", funcionarioRepo);
            JOptionPane.showMessageDialog(this, "Reportes generados en archivos de texto.");
        });
        south.add(btnReporte);

        getContentPane().add(south, BorderLayout.SOUTH);
    }

    private JPanel crearPanelBeneficiarios() {
        JPanel panel = new JPanel(new BorderLayout());

        modeloBeneficiarios = new DefaultListModel<>();
        listaBeneficiarios = new JList<>(modeloBeneficiarios);
        actualizarListaBeneficiarios();

        panel.add(new JScrollPane(listaBeneficiarios), BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(2, 4, 5, 5));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnVerServiciosNotas = new JButton("Ver/Editar Servicios/Notas");
        JButton btnFiltrarZona = new JButton("Filtrar por Zona");
        JButton btnFiltrarDis = new JButton("Filtrar por Discapacidad");
        JButton btnBuscarServicio = new JButton("Buscar por Servicio");
        JButton btnMostrarTodos = new JButton("Mostrar Todos");

        btnAgregar.addActionListener(e -> accionAgregarBeneficiario());
        btnEditar.addActionListener(e -> accionEditarBeneficiario());
        btnEliminar.addActionListener(e -> accionEliminarBeneficiario());
        btnVerServiciosNotas.addActionListener(e -> accionServiciosNotas());
        btnFiltrarZona.addActionListener(e -> accionFiltrarZona());
        btnFiltrarDis.addActionListener(e -> accionFiltrarDiscapacidad());
        btnBuscarServicio.addActionListener(e -> accionBuscarServicio());
        btnMostrarTodos.addActionListener(e -> actualizarListaBeneficiarios());

        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnVerServiciosNotas);
        botones.add(btnFiltrarZona);
        botones.add(btnFiltrarDis);
        botones.add(btnBuscarServicio);
        botones.add(btnMostrarTodos);

        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelFuncionarios() {
        JPanel panel = new JPanel(new BorderLayout());

        modeloFuncionarios = new DefaultListModel<>();
        listaFuncionarios = new JList<>(modeloFuncionarios);
        actualizarListaFuncionarios();

        panel.add(new JScrollPane(listaFuncionarios), BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(2, 3, 5, 5));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnFiltrarZona = new JButton("Filtrar por Zona");
        JButton btnMostrarTodos = new JButton("Mostrar Todos");

        btnAgregar.addActionListener(e -> accionAgregarFuncionario());
        btnEditar.addActionListener(e -> accionEditarFuncionario());
        btnEliminar.addActionListener(e -> accionEliminarFuncionario());
        btnFiltrarZona.addActionListener(e -> accionFiltrarZonaFuncionarios());
        btnMostrarTodos.addActionListener(e -> actualizarListaFuncionarios());

        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnFiltrarZona);
        botones.add(btnMostrarTodos);

        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    // ---------- ACCIONES Beneficiarios ----------
    private void actualizarListaBeneficiarios() {
        modeloBeneficiarios.clear();
        for (Beneficiario b : beneficiarioRepo.obtenerTodos()) {
            modeloBeneficiarios.addElement(b.toString());
        }
    }

    private void accionAgregarBeneficiario() {
        try {
            JTextField rut = new JTextField();
            JTextField nombre = new JTextField();
            JTextField fecha = new JTextField();
            JTextField disc = new JTextField();
            JTextField zona = new JTextField();
            Object[] message = {
                    "RUT (ej: 11111111-1):", rut,
                    "Nombre:", nombre,
                    "Fecha Nac.:", fecha,
                    "Discapacidad:", disc,
                    "Zona:", zona
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Agregar Beneficiario", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Beneficiario b = new Beneficiario(rut.getText().trim(), nombre.getText().trim(), fecha.getText().trim(), disc.getText().trim(), zona.getText().trim());
                    beneficiarioRepo.agregar(b);
                    actualizarListaBeneficiarios();
                } catch (exceptions.RutInvalidoException ex) {
                    JOptionPane.showMessageDialog(this, "RUT inválido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEditarBeneficiario() {
        int idx = listaBeneficiarios.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un beneficiario para editar.");
            return;
        }
        // obtener rut de la cadena mostrada (está en formato Nombre (rut) - ...)
        String selected = modeloBeneficiarios.getElementAt(idx);
        String rut = extraerRutDeToString(selected);
        Beneficiario b = beneficiarioRepo.obtenerPorRut(rut);
        if (b == null) return;

        JTextField nombre = new JTextField(b.getNombre());
        JTextField fecha = new JTextField(b.getFechaNacimiento());
        JTextField disc = new JTextField(b.getDiscapacidad());
        JTextField zona = new JTextField(b.getZona());
        Object[] message = {
                "Nombre:", nombre,
                "Fecha Nac.:", fecha,
                "Discapacidad:", disc,
                "Zona:", zona
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Editar Beneficiario", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            b.setNombre(nombre.getText().trim());
            b.setFechaNacimiento(fecha.getText().trim());
            b.setDiscapacidad(disc.getText().trim());
            b.setZona(zona.getText().trim());
            actualizarListaBeneficiarios();
        }
    }

    private void accionEliminarBeneficiario() {
        int idx = listaBeneficiarios.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un beneficiario para eliminar.");
            return;
        }
        String selected = modeloBeneficiarios.getElementAt(idx);
        String rut = extraerRutDeToString(selected);
        int confirm = JOptionPane.showConfirmDialog(this, "Confirma eliminar beneficiario " + rut + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            beneficiarioRepo.eliminar(rut);
            actualizarListaBeneficiarios();
        }
    }

    private void accionServiciosNotas() {
        int idx = listaBeneficiarios.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un beneficiario para ver servicios/notas.");
            return;
        }
        String selected = modeloBeneficiarios.getElementAt(idx);
        String rut = extraerRutDeToString(selected);
        Beneficiario b = beneficiarioRepo.obtenerPorRut(rut);
        if (b == null) return;
        // Mostrar panel sencillo para listar, agregar, editar, eliminar servicios y notas
        ServicesNotesDialog dialog = new ServicesNotesDialog(this, b);
        dialog.setVisible(true);
        // después de cerrar, actualizar listado
        actualizarListaBeneficiarios();
    }

    private void accionFiltrarZona() {
        String zona = JOptionPane.showInputDialog(this, "Ingrese zona a filtrar:");
        if (zona == null) return;
        List<Beneficiario> filtrados = beneficiarioRepo.filtrarPorZona(zona.trim());
        modeloBeneficiarios.clear();
        for (Beneficiario b : filtrados) modeloBeneficiarios.addElement(b.toString());
    }

    private void accionFiltrarDiscapacidad() {
        String dis = JOptionPane.showInputDialog(this, "Ingrese discapacidad a filtrar:");
        if (dis == null) return;
        List<Beneficiario> filtrados = beneficiarioRepo.filtrarPorDiscapacidad(dis.trim());
        modeloBeneficiarios.clear();
        for (Beneficiario b : filtrados) modeloBeneficiarios.addElement(b.toString());
    }

    private void accionBuscarServicio() {
        String tipo = JOptionPane.showInputDialog(this, "Ingrese tipo de servicio a buscar (ej: Fisioterapia):");
        if (tipo == null) return;
        List<Beneficiario> encontrados = beneficiarioRepo.buscarServicio(tipo.trim());
        modeloBeneficiarios.clear();
        for (Beneficiario b : encontrados) modeloBeneficiarios.addElement(b.toString());
    }

    // ---------- ACCIONES Funcionarios ----------
    private void actualizarListaFuncionarios() {
        modeloFuncionarios.clear();
        for (Funcionario f : funcionarioRepo.obtenerTodos()) {
            modeloFuncionarios.addElement(f.toString());
        }
    }

    private void accionAgregarFuncionario() {
        try {
            JTextField rut = new JTextField();
            JTextField nombre = new JTextField();
            JTextField fecha = new JTextField();
            JTextField area = new JTextField();
            JTextField zona = new JTextField();
            Object[] message = {
                    "RUT (ej: 11111111-1):", rut,
                    "Nombre:", nombre,
                    "Fecha Nac.:", fecha,
                    "Área Trabajo:", area,
                    "Zona:", zona
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Agregar Funcionario", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Funcionario f = new Funcionario(rut.getText().trim(), nombre.getText().trim(), fecha.getText().trim(), area.getText().trim(), zona.getText().trim());
                    funcionarioRepo.agregar(f);
                    actualizarListaFuncionarios();
                } catch (exceptions.RutInvalidoException ex) {
                    JOptionPane.showMessageDialog(this, "RUT inválido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEditarFuncionario() {
        int idx = listaFuncionarios.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un funcionario para editar.");
            return;
        }
        String selected = modeloFuncionarios.getElementAt(idx);
        String rut = extraerRutDeToString(selected);
        Funcionario f = funcionarioRepo.obtenerPorRut(rut);
        if (f == null) return;

        JTextField nombre = new JTextField(f.getNombre());
        JTextField fecha = new JTextField(f.getFechaNacimiento());
        JTextField area = new JTextField(f.getAreaTrabajo());
        JTextField zona = new JTextField(f.getZona());
        Object[] message = {
                "Nombre:", nombre,
                "Fecha Nac.:", fecha,
                "Área Trabajo:", area,
                "Zona:", zona
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Editar Funcionario", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            f.setNombre(nombre.getText().trim());
            f.setFechaNacimiento(fecha.getText().trim());
            f.setAreaTrabajo(area.getText().trim());
            f.setZona(zona.getText().trim());
            actualizarListaFuncionarios();
        }
    }

    private void accionEliminarFuncionario() {
        int idx = listaFuncionarios.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un funcionario para eliminar.");
            return;
        }
        String selected = modeloFuncionarios.getElementAt(idx);
        String rut = extraerRutDeToString(selected);
        int confirm = JOptionPane.showConfirmDialog(this, "Confirma eliminar funcionario " + rut + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            funcionarioRepo.eliminar(rut);
            actualizarListaFuncionarios();
        }
    }

    private void accionFiltrarZonaFuncionarios() {
        String zona = JOptionPane.showInputDialog(this, "Ingrese zona a filtrar:");
        if (zona == null) return;
        List<Funcionario> filtrados = funcionarioRepo.filtrarPorZona(zona.trim());
        modeloFuncionarios.clear();
        for (Funcionario f : filtrados) modeloFuncionarios.addElement(f.toString());
    }

    // ---------- util ----------
    private String extraerRutDeToString(String tostr) {
        // formato esperado: Nombre (rut) - ...
        int p1 = tostr.indexOf('(');
        int p2 = tostr.indexOf(')');
        if (p1 >= 0 && p2 > p1) {
            return tostr.substring(p1 + 1, p2);
        }
        return tostr;
    }
}