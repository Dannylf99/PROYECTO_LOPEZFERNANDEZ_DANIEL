package main.roles;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "registro_horas")
public class RegistroHorasRol {

    public enum Estado {
        PENDIENTE, VALIDADA, RECHAZADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private int idRegistro;

    @ManyToOne
    @JoinColumn(name = "id_practica", nullable = false)
    private PracticaRol practica;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private BigDecimal horas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    public RegistroHorasRol() {}

    // Getters y Setters
    public int getIdRegistro() { return idRegistro; }
    public void setIdRegistro(int idRegistro) { this.idRegistro = idRegistro; }

    public PracticaRol getPractica() { return practica; }
    public void setPractica(PracticaRol practica) { this.practica = practica; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public BigDecimal getHoras() { return horas; }
    public void setHoras(BigDecimal horas) { this.horas = horas; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}
