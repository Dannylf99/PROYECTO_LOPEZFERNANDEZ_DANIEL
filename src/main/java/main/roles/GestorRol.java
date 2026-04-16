package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import main.entities.Usuario;

@Entity
@Table(name = "gestor")
public class GestorRol extends Usuario {

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    // Constructor vacío
    public GestorRol() {
        super();
    }

    // Constructor completo (ACTUALIZADO con DNI)
    public GestorRol(int idUsuario, String nombre, String apellidos, String dni, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, dni, email, contrasenya);
    }

    // Getters y Setters
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    // Implementación de métodos abstractos
    @Override
    public void iniciarSesion() {
        System.out.println("Gestor " + getNombre() + " ha iniciado sesión");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Gestor " + getNombre() + " ha cerrado sesión");
    }
}