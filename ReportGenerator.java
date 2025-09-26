import java.io.*;
import java.util.*;

/**
 * Generador de reportes sencillos en TXT con contenido de los repositorios.
 * Implementa SIA2.10.
 */
public class ReportGenerator {

    public static void generarReporteBeneficiarios(String filename, BeneficiarioRepository repo) {
        List<String> lines = new ArrayList<>();
        lines.add("REPORTE DE BENEFICIARIOS");
        lines.add("=======================");
        for (Beneficiario b : repo.obtenerTodos()) {
            lines.add("RUT: " + b.getRut());
            lines.add("Nombre: " + b.getNombre());
            lines.add("Fecha Nac.: " + b.getFechaNacimiento());
            lines.add("Discapacidad: " + b.getDiscapacidad());
            lines.add("Zona: " + b.getZona());
            lines.add("Servicios:");
            if (b.getServiciosDeApoyo().isEmpty()) {
                lines.add("  (ninguno)");
            } else {
                for (ServiciodeApoyo s : b.getServiciosDeApoyo()) {
                    lines.add("  - " + s.toString());
                }
            }
            lines.add("Notas:");
            if (b.getSeguimientoImpacto().isEmpty()) {
                lines.add("  (ninguna)");
            } else {
                for (SeguimientoImpacto n : b.getSeguimientoImpacto()) {
                    lines.add("  - " + n.getEfecto());
                }
            }
            lines.add("---------------------------");
        }
        write(filename, lines);
    }

    public static void generarReporteFuncionarios(String filename, FuncionarioRepository repo) {
        List<String> lines = new ArrayList<>();
        lines.add("REPORTE DE FUNCIONARIOS");
        lines.add("=======================");
        for (Funcionario f : repo.obtenerTodos()) {
            lines.add("RUT: " + f.getRut());
            lines.add("Nombre: " + f.getNombre());
            lines.add("Fecha Nac.: " + f.getFechaNacimiento());
            lines.add("Área: " + f.getAreaTrabajo());
            lines.add("Zona: " + f.getZona());
            lines.add("---------------------------");
        }
        write(filename, lines);
    }

    private static void write(String filename, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("Reporte generado: " + filename);
        } catch (IOException e) {
            System.err.println("Error generando reporte " + filename + ": " + e.getMessage());
        }
    }
}