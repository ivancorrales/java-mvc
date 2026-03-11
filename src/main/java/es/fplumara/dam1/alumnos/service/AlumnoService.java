package es.fplumara.dam1.alumnos.service;

import es.fplumara.dam1.alumnos.model.Alumno;

import java.util.List;

public interface AlumnoService {

    Alumno getAlumno(Integer idAlumno);

    List<Alumno> getAlumnos();

    Alumno crearAlumno(Alumno alumno);

    Alumno modificarAlumno(Alumno alumno);
}
