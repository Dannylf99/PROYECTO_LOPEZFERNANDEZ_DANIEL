package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.AlumnoRol;
import main.roles.NotificacionRol;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<NotificacionRol, Integer> {
    List<NotificacionRol> findByAlumnoAndBorradaFalseOrderByFechaDesc(AlumnoRol alumno);
    List<NotificacionRol> findByAlumnoAndLeidaFalseAndBorradaFalse(AlumnoRol alumno);
}