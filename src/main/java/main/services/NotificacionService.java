package main.services;

import org.springframework.stereotype.Service;

import main.repositories.NotificacionRepository;
import main.roles.NotificacionRol;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepo;

    public NotificacionService(NotificacionRepository notificacionRepo) {
        this.notificacionRepo = notificacionRepo;
    }

    public List<NotificacionRol> getAllNotificaciones() {
        return notificacionRepo.findAll();
    }

    public NotificacionRol saveNotificacion(NotificacionRol notificacion) {
        return notificacionRepo.save(notificacion);
    }
}
