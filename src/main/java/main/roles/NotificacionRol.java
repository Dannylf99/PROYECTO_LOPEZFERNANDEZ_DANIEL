package main.roles;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notificacion")
public class NotificacionRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int idNotificacion;

    private String mensaje;

    private LocalDate fecha;

    private boolean leida;

    public NotificacionRol() {}

    public NotificacionRol(int idNotificacion, String mensaje, LocalDate fecha, boolean leida) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = leida;
    }

    // Getters y Setters
    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    // Métodos específicos
    public void enviar() {
        System.out.println("Enviando notificación: " + mensaje);
    }

    public void marcarComoLeida() {
        this.leida = true;
        System.out.println("Notificación marcada como leída");
    }
}