package es.fplumara.dam1.alumnos.model;

import java.time.Year;

public class Alumno {

    private Integer id;

    private String nombre;

    private String apellidos;

    private Integer anioNacimiento;

    public Alumno(Integer id, String nombre, String apellidos, Integer anioNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public void validar() {
        if (this.getNombre()==null || this.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (this.getApellidos()==null || this.getApellidos().isBlank()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos");
        }
        if (this.getAnioNacimiento() == null) {
            throw new IllegalArgumentException("El año de nacimiento no puede ser null");
        }

        int year = this.getAnioNacimiento();
        int currentYear = Year.now().getValue();

        if (year < 1926 || year > currentYear) {
            throw new IllegalArgumentException("El año de nacimiento debe estar entre 1926 y " + currentYear);
        }
    }
}
