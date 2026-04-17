package main.roles;

import jakarta.persistence.*;
import main.roles.AlumnoRol;
import java.time.LocalDate;

@Entity
@Table(name = "notificacion")
public class NotificacionRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int idNotificacion;

    @ManyToOne
    @JoinColumn(name = "id_alumno", nullable = false)
    private AlumnoRol alumno;

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private boolean leida = false;

    public NotificacionRol() {}

    public NotificacionRol(AlumnoRol alumno, String mensaje, LocalDate fecha) {
        this.alumno = alumno;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = false;
    }

    public int getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(int idNotificacion) { this.idNotificacion = idNotificacion; }

    public AlumnoRol getAlumno() { return alumno; }
    public void setAlumno(AlumnoRol alumno) { this.alumno = alumno; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }

    public void marcarComoLeida() { this.leida = true; }
}