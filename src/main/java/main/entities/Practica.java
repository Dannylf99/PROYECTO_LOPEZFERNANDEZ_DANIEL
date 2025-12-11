package main.entities;

import jakarta.persistence.*;
import java.sql.Date;

@MappedSuperclass
public abstract class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPractica;

    @ManyToOne
    @JoinColumn(name = "idAlumno")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "idEmpresa")
    private Empresa empresa;

    private Date fechaInicio;
    private Date fechaFin;
    private int horasTotales;

    public Practica() {}

    public Practica(int idPractica, Alumno alumno, Empresa empresa, Date fechaInicio, Date fechaFin, int horasTotales) {
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

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
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

    // Métodos abstractos
    public abstract void iniciarPractica();
    public abstract void finalizarPractica();
}
