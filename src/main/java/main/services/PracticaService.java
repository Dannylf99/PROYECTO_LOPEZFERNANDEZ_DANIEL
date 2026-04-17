package main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.repositories.PracticaRepository;
import main.roles.AlumnoRol;
import main.roles.PracticaRol;

import java.util.List;

@Service
public class PracticaService {

    @Autowired private PracticaRepository practicaRepo;
    @Autowired private NotificacionService notificacionService;

    public List<PracticaRol> getAllPracticas() { return practicaRepo.findAll(); }

    public PracticaRol savePractica(PracticaRol practica) { return practicaRepo.save(practica); }

    public boolean tienesPracticaEnCurso(AlumnoRol alumno) {
        return !practicaRepo.findByAlumnoAndEstado(alumno, PracticaRol.Estado.PREPARADA).isEmpty()
                || !practicaRepo.findByAlumnoAndEstado(alumno, PracticaRol.Estado.ACTIVA).isEmpty();
    }

    public List<PracticaRol> getPracticasByAlumno(AlumnoRol alumno) {
        return practicaRepo.findByAlumno(alumno);
    }

    public void parar(int idPractica) {
        PracticaRol p = practicaRepo.findById(idPractica).orElseThrow();
        p.setEstado(PracticaRol.Estado.PARADA);
        practicaRepo.save(p);
        notificacionService.crearNotificacion(p.getAlumno(),
                "Tus prácticas en " + p.getEmpresa().getNombre() +
                        " han sido pausadas. Contacta con tu coordinador.");
    }

    public void cancelar(int idPractica) {
        PracticaRol p = practicaRepo.findById(idPractica).orElseThrow();
        p.setEstado(PracticaRol.Estado.CANCELADA);
        practicaRepo.save(p);
        notificacionService.crearNotificacion(p.getAlumno(),
                "Tus prácticas en " + p.getEmpresa().getNombre() + " han sido canceladas.");
    }

    public void reanudar(int idPractica) {
        PracticaRol p = practicaRepo.findById(idPractica).orElseThrow();
        p.setEstado(PracticaRol.Estado.ACTIVA);
        practicaRepo.save(p);
        notificacionService.crearNotificacion(p.getAlumno(),
                "Tus prácticas en " + p.getEmpresa().getNombre() + " han sido reanudadas.");
    }
}