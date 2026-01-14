package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.AlumnoRol;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<AlumnoRol, Integer> {
    Optional<AlumnoRol> findByEmailAndContrasenya(String email, String contrasenya);
}