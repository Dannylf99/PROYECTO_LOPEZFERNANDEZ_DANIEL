package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Coordinador;

@Entity
@Table(name = "coordinadores")
public class CoordinadorRol extends Coordinador {

    public CoordinadorRol() {
        super();
    }

    public CoordinadorRol(int idUsuario, String nombre, String apellidos, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
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

