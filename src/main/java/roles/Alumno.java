package roles;

public class Alumno extends Usuario {
private int empresaAsignada;
private int tutorCentro;

public Alumno(int idUsuario, String nombre, String apellidos, String email, String contraseña,
        int empresaAsignada, int tutorCentro) {
	super(idUsuario, nombre, apellidos, email, contraseña);
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
public void iniciarSesion() {
	// TODO Auto-generated method stub
	
}
@Override
public void cerrarSesion() {
	// TODO Auto-generated method stub
	
}

}
