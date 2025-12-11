package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Usuario;

@Entity
@Table(name = "usuarios")
public class UsuarioRol extends Usuario {

    public UsuarioRol() {
        super();
    }

    public UsuarioRol(int idUsuario, String nombre, String apellidos, String email, String contrasena) {
        super(idUsuario, nombre, apellidos, email, contrasena);
    }

    @Override
    public void iniciarSesion() {
        // Implementación de login
    }

    @Override
    public void cerrarSesion() {
        // Implementación de logout
    }
}
