package entities;

import java.sql.Date;

public abstract class Practica {

    private int idPractica;
    private Alumno alumno;
    private Empresa empresa;
    private Date fechaInicio;
    private Date fechaFin;
    private int horasTotales;

    // Constructor vacío opcional (útil para frameworks)
    public Practica() {}

    public Practica(int idPractica, Alumno alumno, Empresa empresa, Date fechaInicio, Date fechaFin, int horasTotales) {
        this.idPractica = idPractica;
        this.alumno = alumno;
        this.empresa = empresa;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasTotales = horasTotales;
    }

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

    // Métodos comunes
    public void registrarHoras() {}

    public void validarHoras() {}

    // Métodos abstractos para que cada práctica implemente su propia lógica
    public abstract void iniciarPractica();

    public abstract void finalizarPractica();
}
