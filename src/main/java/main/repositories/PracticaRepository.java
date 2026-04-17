package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.AlumnoRol;
import main.roles.PracticaRol;

import java.util.List;

public interface PracticaRepository extends JpaRepository<PracticaRol, Integer> {
    List<PracticaRol> findByAlumnoAndEstado(AlumnoRol alumno, PracticaRol.Estado estado);
    List<PracticaRol> findByAlumno(AlumnoRol alumno);
}