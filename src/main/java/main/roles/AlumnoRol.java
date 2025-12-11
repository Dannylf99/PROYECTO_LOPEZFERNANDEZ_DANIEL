package main.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import main.entities.Alumno;

@Entity
@Table(name = "alumnos")
public class AlumnoRol extends Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAlumno;

    public AlumnoRol() {
        super();
    }

    public AlumnoRol(int idAlumno, String nombre, String apellido, String email) {
        super(idAlumno, nombre, apellido, email, email);
        this.idAlumno = idAlumno;
    }

    @Override
    public void asistirClase() {
        // Implementación específica del rol
    }

	@Override
	public void realizarAccion() {
		// TODO Auto-generated method stub
		
	}
}
