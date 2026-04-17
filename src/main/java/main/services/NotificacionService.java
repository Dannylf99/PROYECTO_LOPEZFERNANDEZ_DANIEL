package main.services;

import org.springframework.stereotype.Service;
import main.repositories.NotificacionRepository;
import main.roles.AlumnoRol;
import main.roles.NotificacionRol;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepo;

    public NotificacionService(NotificacionRepository notificacionRepo) {
        this.notificacionRepo = notificacionRepo;
    }

    public List<NotificacionRol> getNotificacionesByAlumno(AlumnoRol alumno) {
        return notificacionRepo.findByAlumnoAndBorradaFalseOrderByFechaDesc(alumno);
    }

    public long countNoLeidas(AlumnoRol alumno) {
        return notificacionRepo.findByAlumnoAndLeidaFalseAndBorradaFalse(alumno).size();
    }

    public void crearNotificacion(AlumnoRol alumno, String mensaje) {
        NotificacionRol notif = new NotificacionRol(alumno, mensaje, LocalDate.now());
        notificacionRepo.save(notif);
    }

    public void marcarLeida(int id) {
        notificacionRepo.findById(id).ifPresent(n -> {
            n.setLeida(true);
            notificacionRepo.save(n);
        });
    }

    public void borrar(int id) {
        // Baja lógica: queda en BD pero no se muestra
        notificacionRepo.findById(id).ifPresent(n -> {
            n.setBorrada(true);
            notificacionRepo.save(n);
        });
    }

    public NotificacionRol saveNotificacion(NotificacionRol notificacion) {
        return notificacionRepo.save(notificacion);
    }
}