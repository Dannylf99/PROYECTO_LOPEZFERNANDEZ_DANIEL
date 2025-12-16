package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import main.entities.Usuario;

@Entity
@Table(name = "alumno")
public class AlumnoRol extends Usuario {

    @Column(name = "empresa_asignada")
    private Integer empresaAsignada;
    
    @Column(name = "tutor_centro")
    private Integer tutorCentro;

    // Constructor vacío
    public AlumnoRol() {
        super();
    }

    // Constructor con parámetros
    public AlumnoRol(int idUsuario, String nombre, String apellidos, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
    }

    // Getters y Setters
    public Integer getEmpresaAsignada() {
        return empresaAsignada;
    }

    public void setEmpresaAsignada(Integer empresaAsignada) {
        this.empresaAsignada = empresaAsignada;
    }

    public Integer getTutorCentro() {
        return tutorCentro;
    }

    public void setTutorCentro(Integer tutorCentro) {
        this.tutorCentro = tutorCentro;
    }

    // Implementación de métodos abstractos
    @Override
    public void iniciarSesion() {
        System.out.println("Alumno " + getNombre() + " ha iniciado sesión");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Alumno " + getNombre() + " ha cerrado sesión");
    }

    // Método específico de alumno
    public void asistirClase() {
        System.out.println("Alumno asistiendo a clase");
    }
}