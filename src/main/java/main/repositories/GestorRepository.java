package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.GestorRol;

import java.util.Optional;

public interface GestorRepository extends JpaRepository<GestorRol, Integer> {
    Optional<GestorRol> findByEmailAndContrasenya(String email, String contrasenya);
}
