package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.EmpresaRol;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<EmpresaRol, Integer> {
    Optional<EmpresaRol> findByCif(String cif);
    Optional<EmpresaRol> findByNombre(String nombre);
}