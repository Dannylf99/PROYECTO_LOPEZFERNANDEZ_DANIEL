package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Administracion;

@Entity
@Table(name = "administracion")
public class AdministracionRol extends Administracion {

    public AdministracionRol() {
        super();
    }

    public AdministracionRol(int idUsuario, String nombre, String apellidos, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
    }

    @Override
    public void ejecutarTareaAdministrativa() {
        // Implementar lógica específica de administración
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

