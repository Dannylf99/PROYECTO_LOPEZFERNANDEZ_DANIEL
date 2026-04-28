package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.AlumnoRol;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<AlumnoRol, Integer> {
    @Query("SELECT a FROM AlumnoRol a WHERE a.idUsuario = :id")
    Optional<AlumnoRol> findByIdUsuario(@Param("id") int id);
    Optional<AlumnoRol> findByDni(String dni);
    Optional<AlumnoRol> findByEmail(String email);
    Optional<AlumnoRol> findByEmailAndContrasenya(String email, String contrasenya);
    List<AlumnoRol> findByActivoTrue();
    Optional<AlumnoRol> findByEmailAndActivoTrue(String email);
}