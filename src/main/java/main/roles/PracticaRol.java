package main.roles;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "practica")
public class PracticaRol {

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

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(name = "horas_totales")
    private int horasTotales;

    public PracticaRol() {}

    public PracticaRol(int idPractica, AlumnoRol alumno, EmpresaRol empresa, Date fechaInicio, Date fechaFin, int horasTotales) {
        this.idPractica = idPractica;
        this.alumno = alumno;
        this.empresa = empresa;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasTotales = horasTotales;
    }

    // Getters y Setters
    public int getIdPractica() {
        return idPractica;
    }

    public void setIdPractica(int idPractica) {
        this.idPractica = idPractica;
    }

    public AlumnoRol getAlumno() {
        return alumno;
    }

    public void setAlumno(AlumnoRol alumno) {
        this.alumno = alumno;
    }

    public EmpresaRol getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaRol empresa) {
        this.empresa = empresa;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getHorasTotales() {
        return horasTotales;
    }

    public void setHorasTotales(int horasTotales) {
        this.horasTotales = horasTotales;
    }

    // Métodos específicos
    public void iniciarPractica() {
        System.out.println("Iniciando práctica para " + alumno.getNombre());
    }

    public void finalizarPractica() {
        System.out.println("Finalizando práctica para " + alumno.getNombre());
    }
}