import exceptions.PersonaNoEncontradaException;
import exceptions.RutInvalidoException;

/**
 * Clase abstracta Persona: superclase para Beneficiario y Funcionario.
 * Contiene atributos comunes como rut, nombre, fechaNacimiento y zona.
 */
public abstract class Persona {
    private String rut;
    private String nombre;
    private String fechaNacimiento;
    private String zona; // Nueva propiedad para filtrar por zona

    public Persona(String rut, String nombre, String fechaNacimiento, String zona) throws RutInvalidoException {
        if (!ValidadorRut.validarRut(rut)) {
            throw new RutInvalidoException("RUT inválido: " + rut);
        }

        this.rut = rut;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.zona = zona;
    }

    // Getters y Setters
    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    // Método abstracto que deben implementar subclases
    public abstract String buscarPorRut(String rut) throws PersonaNoEncontradaException;

    @Override
    public String toString() {
        return nombre + " (" + rut + ") - Zona: " + zona;
    }
}