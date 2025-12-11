package entities;

public abstract class Alumno extends Usuario {

    private int empresaAsignada;
    private int tutorCentro;

    public Alumno() {
        super();
    }

    public Alumno(int idUsuario, String nombre, String apellidos, String email, String contrasenya,
                  int empresaAsignada, int tutorCentro) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
        this.empresaAsignada = empresaAsignada;
        this.tutorCentro = tutorCentro;
    }

    public int getEmpresaAsignada() {
        return empresaAsignada;
    }

    public void setEmpresaAsignada(int empresaAsignada) {
        this.empresaAsignada = empresaAsignada;
    }

    public int getTutorCentro() {
        return tutorCentro;
    }

    public void setTutorCentro(int tutorCentro) {
        this.tutorCentro = tutorCentro;
    }

    @Override
    public abstract void iniciarSesion();

    @Override
    public abstract void cerrarSesion();
}
