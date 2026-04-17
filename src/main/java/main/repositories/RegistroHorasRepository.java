package main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import main.roles.PracticaRol;
import main.roles.RegistroHorasRol;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RegistroHorasRepository extends JpaRepository<RegistroHorasRol, Integer> {

    List<RegistroHorasRol> findByPractica(PracticaRol practica);

    List<RegistroHorasRol> findByPracticaIn(List<PracticaRol> practicas);

    // Suma de horas de una práctica con estado PENDIENTE o VALIDADA
    @Query("SELECT COALESCE(SUM(r.horas), 0) FROM RegistroHorasRol r " +
            "WHERE r.practica = :practica AND r.estado IN ('PENDIENTE', 'VALIDADA')")
    BigDecimal sumHorasActivasByPractica(@Param("practica") PracticaRol practica);

    // Suma de horas de un día concreto en una práctica (PENDIENTE o VALIDADA)
    @Query("SELECT COALESCE(SUM(r.horas), 0) FROM RegistroHorasRol r " +
            "WHERE r.practica = :practica AND r.fecha = :fecha " +
            "AND r.estado IN ('PENDIENTE', 'VALIDADA')")
    BigDecimal sumHorasByPracticaAndFecha(@Param("practica") PracticaRol practica,
                                          @Param("fecha") LocalDate fecha);
}