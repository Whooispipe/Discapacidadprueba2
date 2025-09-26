import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitario simple para leer/escribir CSV.
 * No usa bibliotecas externas para mantener el curso sencillo.
 */
public class CSVUtil {

    // Lee todas las líneas de un archivo CSV si existe
    public static List<String> readAllLines(String filename) {
        List<String> lines = new ArrayList<>();
        File f = new File(filename);
        if (!f.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    // Escribe líneas en un archivo (sobrescribe)
    public static void writeAllLines(String filename, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo " + filename + ": " + e.getMessage());
        }
    }
}