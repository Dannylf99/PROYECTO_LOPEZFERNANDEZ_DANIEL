package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.CoordinadorRol;

import java.util.List;
import java.util.Optional;

public interface CoordinadorRepository extends JpaRepository<CoordinadorRol, Integer> {
    Optional<CoordinadorRol> findByDni(String dni);
    Optional<CoordinadorRol> findByEmail(String email);
    Optional<CoordinadorRol> findByEmailAndContrasenya(String email, String contrasenya);
    List<CoordinadorRol> findByActivoTrue();
    Optional<CoordinadorRol> findByEmailAndActivoTrue(String email);
}