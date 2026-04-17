package main.roles;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "practica")
public class PracticaRol {

    public enum Estado {
        PREPARADA, ACTIVA, FINALIZADA
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

    public PracticaRol(int idPractica, AlumnoRol alumno, EmpresaRol empresa, CoordinadorRol coordinador,
                       Date fechaInicio, Date fechaFin, int horasTotales, int horasHechas, Estado estado) {
        this.idPractica = idPractica;
        this.alumno = alumno;
        this.empresa = empresa;
        this.coordinador = coordinador;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasTotales = horasTotales;
        this.horasHechas = horasHechas;
        this.estado = estado;
    }

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

    // Métodos específicos
    public void iniciarPractica() {
        this.estado = Estado.ACTIVA;
        System.out.println("Iniciando práctica para " + alumno.getNombre());
    }

    public void finalizarPractica() {
        this.estado = Estado.FINALIZADA;
        System.out.println("Finalizando práctica para " + alumno.getNombre());
    }
}