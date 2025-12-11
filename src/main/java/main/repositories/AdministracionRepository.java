package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.AdministracionRol;

import java.util.Optional;

public interface AdministracionRepository extends JpaRepository<AdministracionRol, Integer> {
    Optional<AdministracionRol> findByEmailAndContrasenya(String email, String contrasenya);
}
