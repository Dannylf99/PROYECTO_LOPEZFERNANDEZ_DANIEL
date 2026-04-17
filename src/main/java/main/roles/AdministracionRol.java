package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Usuario;

@Entity
@Table(name = "administracion")
public class AdministracionRol extends Usuario {

    // Constructor vacío
    public AdministracionRol() {
        super();
    }

    // Constructor completo
    public AdministracionRol(int idUsuario, String nombre, String apellidos, String dni, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, dni, email, contrasenya);
    }

    // Implementación de métodos abstractos
    @Override
    public void iniciarSesion() {
        System.out.println("Administrador " + getNombre() + " ha iniciado sesión");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Administrador " + getNombre() + " ha cerrado sesión");
    }
}