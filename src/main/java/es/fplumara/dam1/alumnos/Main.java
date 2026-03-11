package es.fplumara.dam1.alumnos;

import com.sun.net.httpserver.HttpServer;
import es.fplumara.dam1.alumnos.controller.AlumnoController;
import es.fplumara.dam1.alumnos.model.Alumno;
import es.fplumara.dam1.alumnos.repository.AlumnoRepository;
import es.fplumara.dam1.alumnos.repository.AlumnoRepositoryDB;
import es.fplumara.dam1.alumnos.repository.AlumnoRepositoryInMemory;
import es.fplumara.dam1.alumnos.service.AlumnoService;
import es.fplumara.dam1.alumnos.service.AlumnoServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        AlumnoRepository repo = new AlumnoRepositoryDB();
        AlumnoService service = new AlumnoServiceImpl(repo);
        AlumnoController controller = new AlumnoController(service);

        service.crearAlumno(new Alumno(1, "John", "Doe", 2015));
        service.crearAlumno(new Alumno(2, "Jane", "Doe", 2018));
        service.crearAlumno(new Alumno(3, "Tim", "Roe", 2016));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/alumnos", exchange -> {
            controller.listadoAlumnos(exchange);
        });

        server.start();
        System.out.println("Servidor arrancado en http://localhost:8080");
    }
}