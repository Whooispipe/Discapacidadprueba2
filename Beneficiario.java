import java.util.ArrayList;
import java.util.List;

import exceptions.PersonaNoEncontradaException;
import exceptions.RutInvalidoException;

/**
 * Representa a un beneficiario.
 * - Tiene lista de servicios de apoyo y notas de seguimiento.
 * - Implementa sobrecarga de métodos (agregarServicioDeApoyo con distinto set de parámetros).
 * - Sobrescribe buscarPorRut de Persona.
 */
public class Beneficiario extends Persona {
    private String discapacidad;
    private List<ServiciodeApoyo> serviciosDeApoyo;
    private List<SeguimientoImpacto> seguimientoImpacto;

    public Beneficiario(String rut, String nombre, String fechaNacimiento, String discapacidad, String zona) throws RutInvalidoException {
        super(rut, nombre, fechaNacimiento, zona);
        this.discapacidad = discapacidad;
        this.serviciosDeApoyo = new ArrayList<>();
        this.seguimientoImpacto = new ArrayList<>();
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public List<ServiciodeApoyo> getServiciosDeApoyo() {
        return serviciosDeApoyo;
    }

    public void setServiciosDeApoyo(List<ServiciodeApoyo> serviciosDeApoyo) {
        if (serviciosDeApoyo != null) {
            this.serviciosDeApoyo = serviciosDeApoyo;
        }
    }

    public List<SeguimientoImpacto> getSeguimientoImpacto() {
        return seguimientoImpacto;
    }

    public void setSeguimientoImpacto(List<SeguimientoImpacto> seguimientoImpacto) {
        if (seguimientoImpacto != null) {
            this.seguimientoImpacto = seguimientoImpacto;
        }
    }

    // Sobrecarga de métodos (no constructores) - SIA1.6
    public void agregarServicioDeApoyo(ServiciodeApoyo servicio) {
        if (servicio != null) {
            this.serviciosDeApoyo.add(servicio);
        }
    }

    public void agregarServicioDeApoyo(String tipo, String descripcion) {
        ServiciodeApoyo servicio = new ServiciodeApoyo(tipo, descripcion);
        this.serviciosDeApoyo.add(servicio);
    }

    // Sobrecarga para agregar nota: por objeto o por String
    public void agregarNota(SeguimientoImpacto nota) {
        if (nota != null) this.seguimientoImpacto.add(nota);
    }

    public void agregarNota(String efecto) {
        this.seguimientoImpacto.add(new SeguimientoImpacto(efecto));
    }

    // Métodos para editar/eliminar servicios y notas (SIA2.4)
    public boolean eliminarServicio(int index) {
        if (index >= 0 && index < serviciosDeApoyo.size()) {
            serviciosDeApoyo.remove(index);
            return true;
        }
        return false;
    }

    public boolean editarServicio(int index, String nuevoTipo, String nuevaDescripcion) {
        if (index >= 0 && index < serviciosDeApoyo.size()) {
            ServiciodeApoyo s = serviciosDeApoyo.get(index);
            s.setTipoServicio(nuevoTipo);
            s.setDescripcion(nuevaDescripcion);
            return true;
        }
        return false;
    }

    public boolean eliminarNota(int index) {
        if (index >= 0 && index < seguimientoImpacto.size()) {
            seguimientoImpacto.remove(index);
            return true;
        }
        return false;
    }

    public boolean editarNota(int index, String nuevoEfecto) {
        if (index >= 0 && index < seguimientoImpacto.size()) {
            SeguimientoImpacto s = seguimientoImpacto.get(index);
            s.setEfecto(nuevoEfecto);
            return true;
        }
        return false;
    }

    //Sobrecarga de método buscarPorRut (también sobrescritura) - SIA2.7
    @Override
    public String buscarPorRut(String rut) throws PersonaNoEncontradaException {
        if (this.getRut().equalsIgnoreCase(rut)) {
            return "Beneficiario encontrado: " + this.getNombre() + " | Discapacidad: " + discapacidad + " | Zona: " + this.getZona();
        }
        throw new PersonaNoEncontradaException("No se encontró al beneficiario con RUT: " + rut);
    }

    @Override
    public String toString() {
        return super.toString() + " - Discapacidad: " + discapacidad;
    }
}