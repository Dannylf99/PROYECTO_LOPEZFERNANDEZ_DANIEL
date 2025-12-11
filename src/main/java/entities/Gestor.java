package entities;

import java.util.ArrayList;

public abstract class Gestor extends Usuario {

    private int idEmpresa;
    private ArrayList<Alumno> alumnosAsignados = new ArrayList<>();

    public Gestor() {
        super();
    }

    public Gestor(int idUsuario, String nombre, String apellidos, String email, String contrasenya, int idEmpresa) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
        this.idEmpresa = idEmpresa;
    }

    @Override
    public abstract void iniciarSesion();

    @Override
    public abstract void cerrarSesion();

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public ArrayList<Alumno> getAlumnosAsignados() {
        return alumnosAsignados;
    }

    public void setAlumnosAsignados(ArrayList<Alumno> alumnosAsignados) {
        this.alumnosAsignados = alumnosAsignados;
    }


    public void validarHoras() {}

    public void firmarDocumento(Documento documento) {}

    public void revisarDocumento(Documento documento) {}

    
    public abstract void gestionarAlumno(Alumno alumno);

    public abstract void gestionarDocumento(Documento documento);
}
