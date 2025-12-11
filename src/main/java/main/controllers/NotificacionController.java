package main.controllers;

import org.springframework.web.bind.annotation.*;

import main.roles.NotificacionRol;
import main.services.NotificacionService;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<NotificacionRol> getAllNotificaciones() {
        return notificacionService.getAllNotificaciones();
    }

    @PostMapping("/save")
    public NotificacionRol saveNotificacion(@RequestBody NotificacionRol notificacion) {
        return notificacionService.saveNotificacion(notificacion);
    }
}
