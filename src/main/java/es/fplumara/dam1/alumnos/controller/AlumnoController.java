package es.fplumara.dam1.alumnos.controller;

import com.sun.net.httpserver.HttpExchange;
import es.fplumara.dam1.alumnos.model.Alumno;
import es.fplumara.dam1.alumnos.service.AlumnoService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AlumnoController {

    private AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    private void responder(HttpExchange exchange, int codigo, String contenido) throws IOException {
        byte[] bytes = contenido.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(codigo, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public void listadoAlumnos(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido");
            return;
        }

        List<Alumno> alumnos = service.getAlumnos();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < alumnos.size(); i++) {
            Alumno a = alumnos.get(i);
            json.append("{")
                    .append("\"id\":").append(a.getId()).append(",")
                    .append("\"nombre\":\"").append(a.getNombre()).append("\",")
                    .append("\"apellidos\":\"").append(a.getApellidos()).append("\",")
                    .append("\"anioNacimiento\":").append(a.getAnioNacimiento())
                    .append("}");

            if (i < alumnos.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        responder(exchange, 200, json.toString());
    }
}
