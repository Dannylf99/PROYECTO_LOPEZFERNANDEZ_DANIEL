package main.roles;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "practica")
public class PracticaRol {

    public enum Estado {
        PREPARADA, ACTIVA, PARADA, FINALIZADA, CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_practica")
    private int idPractica;

    @ManyToOne
    @JoinColumn(name = "id_alumno")
    private AlumnoRol alumno;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private EmpresaRol empresa;

    @ManyToOne
    @JoinColumn(name = "id_coordinador")
    private CoordinadorRol coordinador;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(name = "horas_totales", nullable = false)
    private int horasTotales;

    @Column(name = "horas_hechas", nullable = false)
    private int horasHechas = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.PREPARADA;

    public PracticaRol() {}

    // Getters y Setters
    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }

    public AlumnoRol getAlumno() { return alumno; }
    public void setAlumno(AlumnoRol alumno) { this.alumno = alumno; }

    public EmpresaRol getEmpresa() { return empresa; }
    public void setEmpresa(EmpresaRol empresa) { this.empresa = empresa; }

    public CoordinadorRol getCoordinador() { return coordinador; }
    public void setCoordinador(CoordinadorRol coordinador) { this.coordinador = coordinador; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public int getHorasTotales() { return horasTotales; }
    public void setHorasTotales(int horasTotales) { this.horasTotales = horasTotales; }

    public int getHorasHechas() { return horasHechas; }
    public void setHorasHechas(int horasHechas) { this.horasHechas = horasHechas; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public void iniciarPractica() { this.estado = Estado.ACTIVA; }
    public void finalizarPractica() { this.estado = Estado.FINALIZADA; }
    public void pararPractica() { this.estado = Estado.PARADA; }
    public void cancelarPractica() { this.estado = Estado.CANCELADA; }
    public void reanudarPractica() { this.estado = Estado.ACTIVA; }
}