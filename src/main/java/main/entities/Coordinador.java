package main.entities;

import java.util.ArrayList;

public abstract class Coordinador extends Usuario {

    private ArrayList<Alumno> alumnosAsignados = new ArrayList<>();

    public Coordinador() {
        super();
    }

    public Coordinador(int idUsuario, String nombre, String apellidos, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
    }

    public ArrayList<Alumno> getAlumnosAsignados() {
        return alumnosAsignados;
    }

    public void setAlumnosAsignados(ArrayList<Alumno> alumnosAsignados) {
        this.alumnosAsignados = alumnosAsignados;
    }

    @Override
    public abstract void iniciarSesion();

    @Override
    public abstract void cerrarSesion();
}
