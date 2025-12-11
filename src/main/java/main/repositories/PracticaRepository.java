package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.PracticaRol;

public interface PracticaRepository extends JpaRepository<PracticaRol, Integer> {
}
