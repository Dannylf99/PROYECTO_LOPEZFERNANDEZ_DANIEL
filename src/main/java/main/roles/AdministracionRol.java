package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import main.entities.Usuario;

@Entity
@Table(name = "administracion")
public class AdministracionRol extends Usuario {

    public AdministracionRol() {
        super();
    }

    public AdministracionRol(int idUsuario, String nombre, String apellidos, String email, String contrasenya) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
    }

    @Override
    public void iniciarSesion() {
        System.out.println("Administrador " + getNombre() + " ha iniciado sesión");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Administrador " + getNombre() + " ha cerrado sesión");
    }

    // Métodos específicos de administración
    public void gestionarUsuarios() {
        System.out.println("Gestionando usuarios...");
    }

    public void invalidarDocumentos() {
        System.out.println("Invalidando documentos...");
    }

    public void exportarInforme() {
        System.out.println("Exportando informe...");
    }

    public void asignarAlumnosCoordinador() {
        System.out.println("Asignando alumnos a coordinador...");
    }

    public void asignarAlumnosEmpresa() {
        System.out.println("Asignando alumnos a empresa...");
    }

    public void ejecutarTareaAdministrativa() {
        System.out.println("Ejecutando tarea administrativa...");
    }
}