package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import main.entities.Usuario;
import java.util.ArrayList;

@Entity
@Table(name = "coordinador")
public class CoordinadorRol extends Usuario {

    @Transient // No se guarda en BD, solo en memoria
    private ArrayList<AlumnoRol> alumnosAsignados = new ArrayList<>();

    public CoordinadorRol() {
        super();
    }

    public CoordinadorRol(int idUsuario, String nombre, String apellidos, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
    }

    @Override
    public void iniciarSesion() {
        System.out.println("Coordinador " + getNombre() + " ha iniciado sesión");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Coordinador " + getNombre() + " ha cerrado sesión");
    }

    // Getters y Setters
    public ArrayList<AlumnoRol> getAlumnosAsignados() {
        return alumnosAsignados;
    }

    public void setAlumnosAsignados(ArrayList<AlumnoRol> alumnosAsignados) {
        this.alumnosAsignados = alumnosAsignados;
    }
}