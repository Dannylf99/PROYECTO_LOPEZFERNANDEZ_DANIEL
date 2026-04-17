package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.GestorRol;

import java.util.List;
import java.util.Optional;

public interface GestorRepository extends JpaRepository<GestorRol, Integer> {
    Optional<GestorRol> findByDni(String dni);
    Optional<GestorRol> findByEmail(String email);
    Optional<GestorRol> findByEmailAndContrasenya(String email, String contrasenya);
    List<GestorRol> findByActivoTrue();
    Optional<GestorRol> findByEmailAndActivoTrue(String email);
    List<GestorRol> findByIdEmpresaAndActivoTrue(Integer idEmpresa);
}