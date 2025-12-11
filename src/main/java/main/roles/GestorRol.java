package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Alumno;
import main.entities.Documento;
import main.entities.Gestor;

@Entity
@Table(name = "gestores")
public class GestorRol extends Gestor {

    public GestorRol() {
        super();
    }

    public GestorRol(int idUsuario, String nombre, String apellidos, String email, String contrasenya,
                     int idEmpresa) {
        super(idUsuario, nombre, apellidos, email, contrasenya, idEmpresa);
    }

	@Override
	public void iniciarSesion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cerrarSesion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gestionarAlumno(Alumno alumno) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gestionarDocumento(Documento documento) {
		// TODO Auto-generated method stub
		
	}
}

