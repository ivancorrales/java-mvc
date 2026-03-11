package es.fplumara.dam1.alumnos.service;


import es.fplumara.dam1.alumnos.model.Alumno;
import es.fplumara.dam1.alumnos.repository.AlumnoRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementación de AlumnoService.
 * Depende de un repositorio (en memoria o BBDD) que se inyecta por constructor.
 */
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = Objects.requireNonNull(alumnoRepository, "alumnoRepository no puede ser null");
    }

    @Override
    public Alumno getAlumno(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser un entero positivo");
        }
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
    }

    @Override
    public List<Alumno> getAlumnos() {
        return alumnoRepository.findAll();
    }

    @Override
    public Alumno crearAlumno(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El alumno no puede ser null");
        }
        alumno.validar();
        return alumnoRepository.insert(alumno);
    }

    @Override
    public Alumno modificarAlumno(Alumno alumno) {
        alumno.validar();
        if (alumno.getId() == null || alumno.getId() <= 0) {
            throw new IllegalArgumentException("El id debe ser un entero positivo");
        }
        Optional<Alumno> alumnoEncontrado = alumnoRepository.findById(alumno.getId());
        if (alumnoEncontrado.isEmpty()) {
            throw new IllegalArgumentException("Alumno no encontrado");
        }

        return alumnoRepository.update(alumno);
    }

}
