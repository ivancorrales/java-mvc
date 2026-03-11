
package es.fplumara.dam1.alumnos.service;

import es.fplumara.dam1.alumnos.model.Alumno;
import es.fplumara.dam1.alumnos.repository.AlumnoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceImplTest {

    @Mock
    AlumnoRepository alumnoRepository;

    @InjectMocks
    AlumnoServiceImpl alumnoService;

    // -------------------------
    // getAlumno
    // -------------------------

    @Test
    void getAlumno_idInvalido_null_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.getAlumno(null));

        assertEquals("El id debe ser un entero positivo", ex.getMessage());
        verifyNoInteractions(alumnoRepository);
    }

    @Test
    void getAlumno_idInvalido_cero_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.getAlumno(0));

        assertEquals("El id debe ser un entero positivo", ex.getMessage());
        verifyNoInteractions(alumnoRepository);
    }

    @Test
    void getAlumno_noExiste_lanzaExcepcion() {
        when(alumnoRepository.findById(10)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.getAlumno(10));

        assertEquals("Alumno no encontrado", ex.getMessage());
        verify(alumnoRepository).findById(10);
        verifyNoMoreInteractions(alumnoRepository);
    }

    @Test
    void getAlumno_existe_devuelveAlumno() {
        Alumno alumno = new Alumno(1, "Ana", "López", 2005);
        when(alumnoRepository.findById(1)).thenReturn(Optional.of(alumno));

        Alumno result = alumnoService.getAlumno(1);

        assertSame(alumno, result);
        verify(alumnoRepository).findById(1);
        verifyNoMoreInteractions(alumnoRepository);
    }

    // -------------------------
    // getAlumnos
    // -------------------------

    @Test
    void getAlumnos_devuelveLista() {
        List<Alumno> lista = List.of(
                new Alumno(1, "Ana", "López", 2005),
                new Alumno(2, "Luis", "Pérez", 2004)
        );
        when(alumnoRepository.findAll()).thenReturn(lista);

        List<Alumno> result = alumnoService.getAlumnos();

        assertEquals(2, result.size());
        assertSame(lista, result);
        verify(alumnoRepository).findAll();
        verifyNoMoreInteractions(alumnoRepository);
    }

    // -------------------------
    // crearAlumno
    // -------------------------

    @Test
    void crearAlumno_alumnoNull_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.crearAlumno(null));

        assertEquals("El alumno no puede ser null", ex.getMessage());
        verifyNoInteractions(alumnoRepository);
    }

    @Test
    void crearAlumno_validacionFalla_lanzaExcepcion_yNoInserta() {
        // nombre en blanco => validar() lanza IllegalArgumentException
        Alumno alumno = new Alumno(null, "  ", "López", 2005);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.crearAlumno(alumno));

        assertEquals("El nombre no puede estar vacío", ex.getMessage());
        verifyNoInteractions(alumnoRepository);
    }

    @Test
    void crearAlumno_ok_llamaInsert_yDevuelveResultadoRepo() {
        Alumno alumnoEntrada = new Alumno(null, "Ana", "López", 2005);
        Alumno alumnoRepo = new Alumno(1, "Ana", "López", 2005);

        when(alumnoRepository.insert(any(Alumno.class))).thenReturn(alumnoRepo);

        Alumno result = alumnoService.crearAlumno(alumnoEntrada);

        assertSame(alumnoRepo, result);

        ArgumentCaptor<Alumno> captor = ArgumentCaptor.forClass(Alumno.class);
        verify(alumnoRepository).insert(captor.capture());
        assertEquals("Ana", captor.getValue().getNombre());
        assertNull(captor.getValue().getId()); // entra sin id en el service
        verifyNoMoreInteractions(alumnoRepository);
    }

    // -------------------------
    // modificarAlumno
    // -------------------------

    @Test
    void modificarAlumno_validacionFalla_lanzaExcepcion_yNoAccedeRepo() {
        // apellidos en blanco => validar() lanza
        Alumno alumno = new Alumno(1, "Ana", "   ", 2005);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.modificarAlumno(alumno));

        assertEquals("Los apellidos no pueden estar vacíos", ex.getMessage());
        verifyNoInteractions(alumnoRepository);
    }

    @Test
    void modificarAlumno_idInvalido_lanzaExcepcion() {
        Alumno alumno = new Alumno(0, "Ana", "López", 2005);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.modificarAlumno(alumno));

        assertEquals("El id debe ser un entero positivo", ex.getMessage());
        verifyNoInteractions(alumnoRepository);
    }

    @Test
    void modificarAlumno_noExiste_lanzaExcepcion_yNoActualiza() {
        Alumno alumno = new Alumno(5, "Ana", "López", 2005);
        when(alumnoRepository.findById(5)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.modificarAlumno(alumno));

        assertEquals("Alumno no encontrado", ex.getMessage());
        verify(alumnoRepository).findById(5);
        verifyNoMoreInteractions(alumnoRepository);
    }

    @Test
    void modificarAlumno_ok_busca_yActualiza() {
        Alumno alumno = new Alumno(5, "Ana", "López", 2005);
        when(alumnoRepository.findById(5)).thenReturn(Optional.of(alumno));
        when(alumnoRepository.update(alumno)).thenReturn(alumno);

        Alumno result = alumnoService.modificarAlumno(alumno);

        assertSame(alumno, result);
        verify(alumnoRepository).findById(5);
        verify(alumnoRepository).update(alumno);
        verifyNoMoreInteractions(alumnoRepository);
    }

    // Extra: año nacimiento fuera de rango (tu validar usa Year.now())
    @Test
    void crearAlumno_anioNacimientoFuturo_lanzaExcepcion() {
        int futuro = Year.now().getValue() + 1;
        Alumno alumno = new Alumno(null, "Ana", "López", futuro);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alumnoService.crearAlumno(alumno));

        assertTrue(ex.getMessage().startsWith("El año de nacimiento debe estar entre"));
        verifyNoInteractions(alumnoRepository);
    }
}
