package es.fplumara.dam1.alumnos.repository;

import es.fplumara.dam1.alumnos.model.Alumno;
import java.util.List;
import java.util.Optional;

public interface AlumnoRepository {

    void initSchema();

    Optional<Alumno> findById(Integer id);

    List<Alumno> findAll();

    Alumno insert(Alumno alumno);

    Alumno update(Alumno alumno);
}
