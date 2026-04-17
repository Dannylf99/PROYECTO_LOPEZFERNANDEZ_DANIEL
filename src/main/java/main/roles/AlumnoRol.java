package main.roles;

import jakarta.persistence.*;
import main.entities.Usuario;

@Entity
@Table(name = "alumno")
public class AlumnoRol extends Usuario {

    public enum Curso {
        DAW, DAM
    }

    public enum Horario {
        MANANA, TARDE, DISTANCIA
    }

    @Column(name = "empresa_asignada")
    private Integer empresaAsignada;

    @Column(name = "tutor_centro")
    private Integer tutorCentro;

    @Enumerated(EnumType.STRING)
    @Column(name = "curso")
    private Curso curso;

    @Enumerated(EnumType.STRING)
    @Column(name = "horario")
    private Horario horario;

    public AlumnoRol() { super(); }

    public AlumnoRol(int idUsuario, String nombre, String apellidos, String dni,
                     String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, dni, email, contrasenya);
    }

    public Integer getEmpresaAsignada() { return empresaAsignada; }
    public void setEmpresaAsignada(Integer empresaAsignada) { this.empresaAsignada = empresaAsignada; }

    public Integer getTutorCentro() { return tutorCentro; }
    public void setTutorCentro(Integer tutorCentro) { this.tutorCentro = tutorCentro; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }

    @Override
    public void iniciarSesion() { System.out.println("Alumno " + getNombre() + " ha iniciado sesión"); }

    @Override
    public void cerrarSesion() { System.out.println("Alumno " + getNombre() + " ha cerrado sesión"); }
}