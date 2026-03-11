package es.fplumara.dam1.alumnos.repository;

import es.fplumara.dam1.alumnos.model.Alumno;

import java.util.*;

public class AlumnoRepositoryInMemory implements AlumnoRepository {

    private Map<Integer, Alumno> data;
    private int nextId = 1;


    @Override
    public void initSchema(){
        data = new HashMap<>();
    }

    @Override
    public Optional<Alumno> findById(Integer id) {
        if (id == null){
            return Optional.empty();
        }
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Alumno> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Alumno insert(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El alumno no puede ser null");
        }
        alumno.setId(nextId++);
        data.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public Alumno update(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El alumno no puede ser null");
        }
        if (alumno.getId() == null || alumno.getId() <= 0) {
            throw new IllegalArgumentException("El id debe ser un entero positivo");
        }
        if (!data.containsKey(alumno.getId())) {
            throw new IllegalArgumentException("No existe alumno con id " + alumno.getId());
        }

        data.put(alumno.getId(), alumno);
        return alumno;
    }
}