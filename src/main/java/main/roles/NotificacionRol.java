package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Notificacion;
import java.time.LocalDate;

@Entity
@Table(name = "notificaciones")
public class NotificacionRol extends Notificacion {

    public NotificacionRol() {
        super();
    }

    public NotificacionRol(int idNotificacion, String mensaje, LocalDate fecha, boolean leida) {
        super(idNotificacion, mensaje, fecha, leida);
    }

    @Override
    public void enviar() {
        // Lógica de envío de notificación
    }
}

