package main.controllers;

import jakarta.servlet.http.HttpSession;
import main.roles.AlumnoRol;
import main.roles.NotificacionRol;
import main.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    // Obtener notificaciones del alumno en sesión
    @GetMapping
    public List<NotificacionRol> getNotificaciones(HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof AlumnoRol)) return List.of();
        return notificacionService.getNotificacionesByAlumno((AlumnoRol) usuario);
    }

    // Marcar una notificación como leída
    @PostMapping("/leer/{id}")
    public void marcarLeida(@PathVariable int id) {
        notificacionService.marcarLeida(id);
    }
}
