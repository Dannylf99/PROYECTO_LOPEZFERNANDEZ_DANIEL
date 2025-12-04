package roles;

import java.util.ArrayList;

public class Coordinador extends Usuario {
	private ArrayList<Alumno> alumnosAsignados;

	public Coordinador(int idUsuario, String nombre, String apellidos, String email, String contraseña) {
		super(idUsuario, nombre, apellidos, email, contraseña);
	}

	@Override
	public void iniciarSesion() {
		//Prueba
	}

	@Override
	public void cerrarSesion() {
	
		
	}

	public void validarDocumento() {
		
	}
	
	public void firmarDocumento() {
		
	}
	
	public void generarInforme() {
		
	}

	public ArrayList<Alumno> getAlumnosAsignados() {
		return alumnosAsignados;
	}

	public void setAlumnosAsignados(ArrayList<Alumno> alumnosAsignados) {
		this.alumnosAsignados = alumnosAsignados;
	}
}
