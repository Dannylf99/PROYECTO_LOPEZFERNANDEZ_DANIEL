package main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.repositories.RegistroHorasRepository;
import main.repositories.PracticaRepository;
import main.roles.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RegistroHorasService {

    @Autowired private RegistroHorasRepository registroRepo;
    @Autowired private PracticaRepository practicaRepo;
    @Autowired private NotificacionService notificacionService;

    private static final BigDecimal MAX_HORAS_DIA = BigDecimal.valueOf(10);

    public RegistroHorasRol registrar(PracticaRol practica, LocalDate fecha,
                                      LocalTime horaInicio, LocalTime horaFin,
                                      LocalTime pausaInicio, LocalTime pausaFin) {

        long minutosTotales = Duration.between(horaInicio, horaFin).toMinutes();
        if (pausaInicio != null && pausaFin != null) {
            minutosTotales -= Duration.between(pausaInicio, pausaFin).toMinutes();
        }
        if (minutosTotales <= 0)
            throw new IllegalArgumentException("Las horas resultantes deben ser positivas.");

        BigDecimal horasNuevas = BigDecimal.valueOf(minutosTotales)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // Validar límite diario (pendientes + validadas del mismo día)
        BigDecimal horasDia = registroRepo.sumHorasByPracticaAndFecha(practica, fecha);
        if (horasDia.add(horasNuevas).compareTo(MAX_HORAS_DIA) > 0) {
            BigDecimal disponibles = MAX_HORAS_DIA.subtract(horasDia);
            throw new IllegalArgumentException(
                    "Límite diario de 10 horas superado. Solo puedes registrar " +
                            disponibles.setScale(2, RoundingMode.HALF_UP) + "h más hoy.");
        }

        // Validar que no supere el total de la práctica (pendientes + validadas)
        BigDecimal horasAcumuladas = registroRepo.sumHorasActivasByPractica(practica);
        BigDecimal horasTotales = BigDecimal.valueOf(practica.getHorasTotales());
        if (horasAcumuladas.add(horasNuevas).compareTo(horasTotales) > 0) {
            BigDecimal disponibles = horasTotales.subtract(horasAcumuladas);
            throw new IllegalArgumentException(
                    "No puedes registrar más horas de las asignadas. Solo quedan " +
                            disponibles.setScale(2, RoundingMode.HALF_UP) + "h disponibles.");
        }

        RegistroHorasRol registro = new RegistroHorasRol();
        registro.setPractica(practica);
        registro.setFecha(fecha);
        registro.setHoraInicio(horaInicio);
        registro.setHoraFin(horaFin);
        registro.setPausaInicio(pausaInicio);
        registro.setPausaFin(pausaFin);
        registro.setHoras(horasNuevas);
        registro.setEstado(RegistroHorasRol.Estado.PENDIENTE);
        return registroRepo.save(registro);
    }

    public void validar(int idRegistro) {
        RegistroHorasRol registro = registroRepo.findById(idRegistro).orElseThrow();
        registro.setEstado(RegistroHorasRol.Estado.VALIDADA);
        registroRepo.save(registro);

        PracticaRol practica = registro.getPractica();
        practica.setHorasHechas(practica.getHorasHechas() + registro.getHoras().intValue());
        practicaRepo.save(practica);

        // Notificar si se han completado todas las horas validadas
        if (practica.getHorasHechas() >= practica.getHorasTotales()) {
            notificacionService.crearNotificacion(practica.getAlumno(),
                    "¡Enhorabuena! Has completado todas las horas de prácticas en " +
                            practica.getEmpresa().getNombre() + " (" + practica.getHorasTotales() + "h).");
        }
    }

    public void rechazar(int idRegistro) {
        RegistroHorasRol registro = registroRepo.findById(idRegistro).orElseThrow();
        registro.setEstado(RegistroHorasRol.Estado.RECHAZADA);
        registroRepo.save(registro);

        notificacionService.crearNotificacion(registro.getPractica().getAlumno(),
                "Tus horas del día " + registro.getFecha() +
                        " (" + registro.getHoras() + "h) han sido rechazadas.");
    }

    public List<RegistroHorasRol> getByPractica(PracticaRol practica) {
        return registroRepo.findByPractica(practica);
    }

    public List<RegistroHorasRol> getPendientesByPracticas(List<PracticaRol> practicas) {
        return registroRepo.findByPracticaIn(practicas).stream()
                .filter(r -> r.getEstado() == RegistroHorasRol.Estado.PENDIENTE)
                .toList();
    }

    // Horas disponibles para registrar en una práctica (total - pendientes - validadas)
    public BigDecimal getHorasDisponibles(PracticaRol practica) {
        BigDecimal acumuladas = registroRepo.sumHorasActivasByPractica(practica);
        return BigDecimal.valueOf(practica.getHorasTotales()).subtract(acumuladas);
    }
}