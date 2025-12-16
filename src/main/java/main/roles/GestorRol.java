package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import main.entities.Usuario;
import java.util.ArrayList;

@Entity
@Table(name = "gestor")
public class GestorRol extends Usuario {

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Transient // No se guarda en BD
    private ArrayList<AlumnoRol> alumnosAsignados = new ArrayList<>();

    public GestorRol() {
        super();
    }

    public GestorRol(int idUsuario, String nombre, String apellidos, String email, String contrasenya, Integer idEmpresa) {
        super(idUsuario, nombre, apellidos, email, contrasenya);
        this.idEmpresa = idEmpresa;
    }

    @Override
    public void iniciarSesion() {
        System.out.println("Gestor " + getNombre() + " ha iniciado sesión");
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Gestor " + getNombre() + " ha cerrado sesión");
    }

    // Getters y Setters
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public ArrayList<AlumnoRol> getAlumnosAsignados() {
        return alumnosAsignados;
    }

    public void setAlumnosAsignados(ArrayList<AlumnoRol> alumnosAsignados) {
        this.alumnosAsignados = alumnosAsignados;
    }

    // Métodos específicos del gestor
    public void validarHoras() {
        System.out.println("Validando horas...");
    }

    public void firmarDocumento(DocumentoRol documento) {
        System.out.println("Firmando documento...");
    }

    public void revisarDocumento(DocumentoRol documento) {
        System.out.println("Revisando documento...");
    }

    public void gestionarAlumno(AlumnoRol alumno) {
        System.out.println("Gestionando alumno: " + alumno.getNombre());
    }

    public void gestionarDocumento(DocumentoRol documento) {
        System.out.println("Gestionando documento...");
    }
}