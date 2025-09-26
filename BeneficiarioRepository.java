import java.util.*;
import exceptions.RutInvalidoException;

/**
 * Repositorio simple que carga y guarda beneficiarios, servicios y notas en CSV.
 * Formatos:
 * - beneficiarios.csv => rut;nombre;fechaNacimiento;discapacidad;zona
 * - servicios.csv => rutBeneficiario;tipo;descripcion
 * - notas.csv => rutBeneficiario;efecto
 *
 * Maneja persistencia en inicio y al guardar.
 */
public class BeneficiarioRepository {
    private Map<String, Beneficiario> mapa = new HashMap<>();
    private final String fileBeneficiarios = "beneficiarios.csv";
    private final String fileServicios = "servicios.csv";
    private final String fileNotas = "notas.csv";

    public BeneficiarioRepository() {
        cargar();
    }

    public Collection<Beneficiario> obtenerTodos() {
        return mapa.values();
    }

    public Beneficiario obtenerPorRut(String rut) {
        return mapa.get(rut);
    }

    public void agregar(Beneficiario b) {
        mapa.put(b.getRut(), b);
    }

    public void eliminar(String rut) {
        mapa.remove(rut);
    }

    public void guardar() {
        // Guardar beneficiarios
        List<String> lines = new ArrayList<>();
        for (Beneficiario b : mapa.values()) {
            String l = String.join(";", escape(b.getRut()), escape(b.getNombre()), escape(b.getFechaNacimiento()), escape(b.getDiscapacidad()), escape(b.getZona()));
            lines.add(l);
        }
        CSVUtil.writeAllLines(fileBeneficiarios, lines);

        // Guardar servicios
        List<String> servLines = new ArrayList<>();
        for (Beneficiario b : mapa.values()) {
            for (ServiciodeApoyo s : b.getServiciosDeApoyo()) {
                String l = String.join(";", escape(b.getRut()), escape(s.getTipoServicio()), escape(s.getDescripcion()));
                servLines.add(l);
            }
        }
        CSVUtil.writeAllLines(fileServicios, servLines);

        // Guardar notas
        List<String> notaLines = new ArrayList<>();
        for (Beneficiario b : mapa.values()) {
            for (SeguimientoImpacto n : b.getSeguimientoImpacto()) {
                String l = String.join(";", escape(b.getRut()), escape(n.getEfecto()));
                notaLines.add(l);
            }
        }
        CSVUtil.writeAllLines(fileNotas, notaLines);
    }

    private void cargar() {
        mapa.clear();
        // cargar beneficiarios
        List<String> lines = CSVUtil.readAllLines(fileBeneficiarios);
        for (String l : lines) {
            String[] parts = l.split(";", -1);
            if (parts.length >= 5) {
                try {
                    Beneficiario b = new Beneficiario(unescape(parts[0]), unescape(parts[1]), unescape(parts[2]), unescape(parts[3]), unescape(parts[4]));
                    mapa.put(b.getRut(), b);
                } catch (RutInvalidoException e) {
                    System.err.println("RUT inválido al cargar beneficiario: " + parts[0]);
                }
            }
        }

        // cargar servicios
        List<String> servLines = CSVUtil.readAllLines(fileServicios);
        for (String l : servLines) {
            String[] parts = l.split(";", -1);
            if (parts.length >= 3) {
                String rut = unescape(parts[0]);
                Beneficiario b = mapa.get(rut);
                if (b != null) {
                    b.agregarServicioDeApoyo(unescape(parts[1]), unescape(parts[2]));
                }
            }
        }

        // cargar notas
        List<String> notaLines = CSVUtil.readAllLines(fileNotas);
        for (String l : notaLines) {
            String[] parts = l.split(";", -1);
            if (parts.length >= 2) {
                String rut = unescape(parts[0]);
                Beneficiario b = mapa.get(rut);
                if (b != null) {
                    b.agregarNota(unescape(parts[1]));
                }
            }
        }
    }

    // Búsquedas útiles (SIA2.5, SIA2.13)
    public List<Beneficiario> filtrarPorZona(String zona) {
        List<Beneficiario> res = new ArrayList<>();
        for (Beneficiario b : mapa.values()) {
            if (b.getZona() != null && b.getZona().equalsIgnoreCase(zona)) res.add(b);
        }
        return res;
    }

    public List<Beneficiario> filtrarPorDiscapacidad(String discapacidad) {
        List<Beneficiario> res = new ArrayList<>();
        for (Beneficiario b : mapa.values()) {
            if (b.getDiscapacidad() != null && b.getDiscapacidad().equalsIgnoreCase(discapacidad)) res.add(b);
        }
        return res;
    }

    public List<Beneficiario> buscarServicio(String tipoServicio) {
        List<Beneficiario> res = new ArrayList<>();
        for (Beneficiario b : mapa.values()) {
            for (ServiciodeApoyo s : b.getServiciosDeApoyo()) {
                if (s.getTipoServicio().equalsIgnoreCase(tipoServicio)) {
                    res.add(b);
                    break;
                }
            }
        }
        return res;
    }

    public List<String> buscarNotasPorEfecto(String efecto) {
        List<String> res = new ArrayList<>();
        for (Beneficiario b : mapa.values()) {
            for (SeguimientoImpacto n : b.getSeguimientoImpacto()) {
                if (n.getEfecto().equalsIgnoreCase(efecto)) {
                    res.add(b.getRut() + ";" + b.getNombre() + ";" + n.getEfecto());
                }
            }
        }
        return res;
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace(";", "\\;");
    }

    private String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\;", ";");
    }
}