package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.AdministracionRol;

import java.util.List;
import java.util.Optional;

public interface AdministracionRepository extends JpaRepository<AdministracionRol, Integer> {
    Optional<AdministracionRol> findByDni(String dni);
    Optional<AdministracionRol> findByEmail(String email);
    Optional<AdministracionRol> findByEmailAndContrasenya(String email, String contrasenya);
    List<AdministracionRol> findByActivoTrue();
    Optional<AdministracionRol> findByEmailAndActivoTrue(String email);
}