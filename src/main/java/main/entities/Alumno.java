package main.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAlumno;

    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    public Alumno() {}

    public Alumno(int idAlumno, String nombre, String apellidos, String email, String telefono) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Métodos abstractos que se puedan necesitar según el rol
    public abstract void realizarAccion();

	public void asistirClase() {
		// TODO Auto-generated method stub
		
	}
}
