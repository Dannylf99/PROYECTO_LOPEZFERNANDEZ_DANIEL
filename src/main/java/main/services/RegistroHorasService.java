package main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.repositories.RegistroHorasRepository;
import main.repositories.PracticaRepository;
import main.roles.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RegistroHorasService {

    @Autowired private RegistroHorasRepository registroRepo;
    @Autowired private PracticaRepository practicaRepo;
    @Autowired private NotificacionService notificacionService;

    // Registrar horas (alumno)
    public RegistroHorasRol registrar(PracticaRol practica, LocalDate fecha,
                                      LocalTime horaInicio, LocalTime horaFin) {
        // Calcular horas automáticamente
        long minutos = java.time.Duration.between(horaInicio, horaFin).toMinutes();
        BigDecimal horas = BigDecimal.valueOf(minutos).divide(BigDecimal.valueOf(60), 2,
                java.math.RoundingMode.HALF_UP);

        RegistroHorasRol registro = new RegistroHorasRol();
        registro.setPractica(practica);
        registro.setFecha(fecha);
        registro.setHoraInicio(horaInicio);
        registro.setHoraFin(horaFin);
        registro.setHoras(horas);
        registro.setEstado(RegistroHorasRol.Estado.PENDIENTE);
        return registroRepo.save(registro);
    }

    // Validar horas (gestor o coordinador)
    public void validar(int idRegistro) {
        RegistroHorasRol registro = registroRepo.findById(idRegistro).orElseThrow();
        registro.setEstado(RegistroHorasRol.Estado.VALIDADA);
        registroRepo.save(registro);

        // Sumar horas a la práctica
        PracticaRol practica = registro.getPractica();
        int nuevasHoras = practica.getHorasHechas() + registro.getHoras().intValue();
        practica.setHorasHechas(nuevasHoras);
        practicaRepo.save(practica);
    }

    // Rechazar horas (gestor o coordinador)
    public void rechazar(int idRegistro) {
        RegistroHorasRol registro = registroRepo.findById(idRegistro).orElseThrow();
        registro.setEstado(RegistroHorasRol.Estado.RECHAZADA);
        registroRepo.save(registro);

        // Notificar al alumno
        AlumnoRol alumno = registro.getPractica().getAlumno();
        String mensaje = "Tus horas del día " + registro.getFecha()
                + " (" + registro.getHoras() + "h) han sido rechazadas.";
        notificacionService.crearNotificacion(alumno, mensaje);
    }

    public List<RegistroHorasRol> getByPractica(PracticaRol practica) {
        return registroRepo.findByPractica(practica);
    }

    public List<RegistroHorasRol> getPendientesByPracticas(List<PracticaRol> practicas) {
        return registroRepo.findByPracticaIn(practicas).stream()
                .filter(r -> r.getEstado() == RegistroHorasRol.Estado.PENDIENTE)
                .toList();
    }
}
