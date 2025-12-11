package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import main.roles.EmpresaRol;

public interface EmpresaRepository extends JpaRepository<EmpresaRol, Integer> {
}
