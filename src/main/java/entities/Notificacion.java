package entities;

import java.time.LocalDate;

public abstract class Notificacion {

    private int idNotificacion;
    private String mensaje;
    private LocalDate fecha;
    private boolean leida;

    public Notificacion() {}

    public Notificacion(int idNotificacion, String mensaje, LocalDate fecha, boolean leida) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = leida;
    }

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

    public void enviar() {}

    public void marcarComoLeida() {
        this.leida = true;
    }

    public abstract void notificarUsuario();
}
