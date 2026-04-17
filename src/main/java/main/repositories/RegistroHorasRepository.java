package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.roles.PracticaRol;
import main.roles.RegistroHorasRol;

import java.util.List;

public interface RegistroHorasRepository extends JpaRepository<RegistroHorasRol, Integer> {
    List<RegistroHorasRol> findByPractica(PracticaRol practica);
    List<RegistroHorasRol> findByPracticaAndEstado(PracticaRol practica, RegistroHorasRol.Estado estado);
    List<RegistroHorasRol> findByEstado(RegistroHorasRol.Estado estado);
    List<RegistroHorasRol> findByPracticaIn(List<PracticaRol> practicas);
}
