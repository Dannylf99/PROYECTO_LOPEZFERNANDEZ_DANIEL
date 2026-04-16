package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Usuario;

@Entity
@Table(name = "coordinador")
public class CoordinadorRol extends Usuario {

    // Constructor vacío
    public CoordinadorRol() {
        super();
    }

    // Constructor completo (ACTUALIZADO con DNI)
    public CoordinadorRol(int idUsuario, String nombre, String apellidos, String dni, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, dni, email, contrasenya);
    }

    // Implementación de métodos abstractos
    @Override
    public void iniciarSesion() {
        System.out.println("Coordinador " + getNombre() + " ha iniciado sesión");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Coordinador " + getNombre() + " ha cerrado sesión");
    }
}