import java.util.*;
import exceptions.RutInvalidoException;

/**
 * Repositorio simple para funcionarios (persistencia en funcionarios.csv).
 * Formato:
 * rut;nombre;fechaNacimiento;areaTrabajo;zona
 */
public class FuncionarioRepository {
    private Map<String, Funcionario> mapa = new HashMap<>();
    private final String fileFuncionarios = "funcionarios.csv";

    public FuncionarioRepository() {
        cargar();
    }

    public Collection<Funcionario> obtenerTodos() {
        return mapa.values();
    }

    public Funcionario obtenerPorRut(String rut) {
        return mapa.get(rut);
    }

    public void agregar(Funcionario f) {
        mapa.put(f.getRut(), f);
    }

    public void eliminar(String rut) {
        mapa.remove(rut);
    }

    public void guardar() {
        List<String> lines = new ArrayList<>();
        for (Funcionario f : mapa.values()) {
            String l = String.join(";", escape(f.getRut()), escape(f.getNombre()), escape(f.getFechaNacimiento()), escape(f.getAreaTrabajo()), escape(f.getZona()));
            lines.add(l);
        }
        CSVUtil.writeAllLines(fileFuncionarios, lines);
    }

    private void cargar() {
        mapa.clear();
        List<String> lines = CSVUtil.readAllLines(fileFuncionarios);
        for (String l : lines) {
            String[] parts = l.split(";", -1);
            if (parts.length >= 5) {
                try {
                    Funcionario f = new Funcionario(unescape(parts[0]), unescape(parts[1]), unescape(parts[2]), unescape(parts[3]), unescape(parts[4]));
                    mapa.put(f.getRut(), f);
                } catch (RutInvalidoException e) {
                    System.err.println("RUT inválido al cargar funcionario: " + parts[0]);
                }
            }
        }
    }

    public List<Funcionario> filtrarPorZona(String zona) {
        List<Funcionario> res = new ArrayList<>();
        for (Funcionario f : mapa.values()) {
            if (f.getZona() != null && f.getZona().equalsIgnoreCase(zona)) res.add(f);
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