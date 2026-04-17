package main.config;

import main.repositories.PracticaRepository;
import main.roles.PracticaRol;
import main.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
public class PracticaScheduler {

    @Autowired private PracticaRepository practicaRepository;
    @Autowired private NotificacionService notificacionService;

    @EventListener(ApplicationReadyEvent.class)
    public void activarAlArrancar() {
        System.out.println("🔄 Comprobando estado de prácticas al arrancar...");
        activarPracticasPendientes();
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void activarPorSchedule() {
        System.out.println("🔄 Comprobando estado de prácticas (scheduler 6am)...");
        activarPracticasPendientes();
    }

    public void activarPracticasPendientes() {
        LocalDate hoy = LocalDate.now();

        // Activar PREPARADAS cuya fecha de inicio ya llegó
        List<PracticaRol> preparadas = practicaRepository.findByEstado(PracticaRol.Estado.PREPARADA);
        for (PracticaRol p : preparadas) {
            if (p.getFechaInicio() != null && !hoy.isBefore(p.getFechaInicio().toLocalDate())) {
                p.setEstado(PracticaRol.Estado.ACTIVA);
                practicaRepository.save(p);

                // Notificar al alumno
                notificacionService.crearNotificacion(p.getAlumno(),
                        "¡Tus prácticas en " + p.getEmpresa().getNombre() +
                                " han comenzado! Ya puedes empezar a registrar tus horas.");

                System.out.println("✅ Práctica activada: id=" + p.getIdPractica()
                        + " alumno=" + p.getAlumno().getNombre());
            }
        }

        // Finalizar ACTIVAS cuya fecha de fin ya pasó
        List<PracticaRol> activas = practicaRepository.findByEstado(PracticaRol.Estado.ACTIVA);
        for (PracticaRol p : activas) {
            if (p.getFechaFin() != null && hoy.isAfter(p.getFechaFin().toLocalDate())) {
                p.setEstado(PracticaRol.Estado.FINALIZADA);
                practicaRepository.save(p);
                System.out.println("✅ Práctica finalizada: id=" + p.getIdPractica()
                        + " alumno=" + p.getAlumno().getNombre());
            }
        }
    }
}