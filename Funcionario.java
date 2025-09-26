import exceptions.PersonaNoEncontradaException;
import exceptions.RutInvalidoException;

/**
 * Representa a un funcionario.
 * Incluye sobrecarga de métodos (asignarZona) para cumplir SIA1.6.
 * Sobrescribe buscarPorRut (SIA2.7).
 */
public class Funcionario extends Persona {
    private String areaTrabajo;

    public Funcionario(String rut, String nombre, String fechaNacimiento, String areaTrabajo, String zona) throws RutInvalidoException {
        super(rut, nombre, fechaNacimiento, zona);
        this.areaTrabajo = areaTrabajo;
    }

    public String getAreaTrabajo() {
        return areaTrabajo;
    }

    public void setAreaTrabajo(String areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }

    // Sobrecarga de métodos: asignarZona (dos firmas diferentes) - SIA1.6
    public void asignarZona(String zona) {
        this.setZona(zona);
    }

    public void asignarZona(String zona, String puestoRelacionado) {
        // ejemplo sencillo: concatenar puesto al dato de zona para tener más info
        this.setZona(zona + " - " + puestoRelacionado);
    }

    @Override
    public String buscarPorRut(String rut) throws PersonaNoEncontradaException {
        if (this.getRut().equalsIgnoreCase(rut)) {
            return "Funcionario encontrado: " + this.getNombre() + " | Área de Trabajo: " + areaTrabajo + " | Zona: " + this.getZona();
        }
        throw new PersonaNoEncontradaException("No se encontró al funcionario con RUT: " + rut);
    }

    @Override
    public String toString() {
        return super.toString() + " - Área: " + areaTrabajo;
    }
}