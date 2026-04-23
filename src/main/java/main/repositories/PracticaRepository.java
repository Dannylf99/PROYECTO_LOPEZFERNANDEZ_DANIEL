package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import main.roles.AlumnoRol;
import main.roles.PracticaRol;

import java.util.List;

public interface PracticaRepository extends JpaRepository<PracticaRol, Integer> {
    List<PracticaRol> findByAlumnoAndEstado(AlumnoRol alumno, PracticaRol.Estado estado);
    List<PracticaRol> findByAlumno(AlumnoRol alumno);
    List<PracticaRol> findByEstado(PracticaRol.Estado estado);

    @Query("SELECT p FROM PracticaRol p WHERE p.empresa.idEmpresa = :idEmpresa AND p.estado = :estado")
    List<PracticaRol> findByEmpresaIdAndEstado(@Param("idEmpresa") int idEmpresa,
                                               @Param("estado") PracticaRol.Estado estado);

    @Query("SELECT p FROM PracticaRol p WHERE p.alumno.idUsuario = :idAlumno")
    List<PracticaRol> findByAlumnoId(@Param("idAlumno") int idAlumno);
}