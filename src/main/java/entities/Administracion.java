package entities;

public abstract class Administracion extends Usuario {

    
    public Administracion() {
        super();
    }

    public Administracion(int idUsuario, String nombre, String apellidos, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
    }

    @Override
    public abstract void iniciarSesion();

    @Override
    public abstract void cerrarSesion();

    
    public void gestionarUsuarios() {}

    public void invalidarDocumentos() {}

    public void exportarInforme() {}

    public void asignarAlumnosCoordinador() {}

    public void asignarAlumnosEmpresa() {}

   
    public abstract void ejecutarTareaAdministrativa();
}
