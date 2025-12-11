package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.CoordinadorRol;

import java.util.Optional;

public interface CoordinadorRepository extends JpaRepository<CoordinadorRol, Integer> {
    Optional<CoordinadorRol> findByEmailAndContrasenya(String email, String contrasenya);
}
