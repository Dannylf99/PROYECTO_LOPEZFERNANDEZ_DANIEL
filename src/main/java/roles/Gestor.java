package roles;

import java.util.ArrayList;

public class Gestor extends Usuario{
private int idEmpresa;
private ArrayList<Alumno> alumnosAsignados;

public Gestor(int idUsuario, String nombre, String apellidos, String email, String contraseña, int idEmpresa) {
	super(idUsuario, nombre, apellidos, email, contraseña);
	this.setIdEmpresa(idEmpresa);
}

@Override
public void iniciarSesion() {
	// TODO Auto-generated method stub
	
}
@Override
public void cerrarSesion() {
	// TODO Auto-generated method stub
	
}

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

public void validarHoras() {
	
}

public void firmarDocumento(Documento documento) {
	
}

public void revisarDocumento(Documento documento) {
	
}

}
