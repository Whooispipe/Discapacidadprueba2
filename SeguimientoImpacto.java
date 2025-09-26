/**
 * Nota de seguimiento/impacto sobre un beneficiario.
 * Mantengo la sobrecarga de constructores existente (no cuenta para SIA1.6),
 * pero el resto de funcionalidades de notas están en Beneficiario.
 */
public class SeguimientoImpacto {
    private String efecto; // "Positivo" o "Negativo" u otro texto

    public SeguimientoImpacto(String efecto) {
        this.efecto = efecto;
    }

    // Sobrecarga 
    public SeguimientoImpacto(boolean positivo) {
        this.efecto = positivo ? "Positivo" : "Negativo";
    }

    // Getters y Setters
    public String getEfecto() {
        return efecto;
    }

    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }

    @Override
    public String toString() {
        return efecto;
    }
}