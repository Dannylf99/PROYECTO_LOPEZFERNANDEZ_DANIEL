package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.DocumentoRol;

public interface DocumentoRepository extends JpaRepository<DocumentoRol, Integer> {
}
