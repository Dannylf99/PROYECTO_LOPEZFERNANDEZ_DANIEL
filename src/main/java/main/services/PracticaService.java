package main.services;

import org.springframework.stereotype.Service;
import main.repositories.PracticaRepository;
import main.roles.AlumnoRol;
import main.roles.PracticaRol;

import java.util.List;

@Service
public class PracticaService {

    private final PracticaRepository practicaRepo;

    public PracticaService(PracticaRepository practicaRepo) {
        this.practicaRepo = practicaRepo;
    }

    public List<PracticaRol> getAllPracticas() {
        return practicaRepo.findAll();
    }

    public PracticaRol savePractica(PracticaRol practica) {
        return practicaRepo.save(practica);
    }

    public boolean tienesPracticaEnCurso(AlumnoRol alumno) {
        List<PracticaRol> preparadas = practicaRepo.findByAlumnoAndEstado(alumno, PracticaRol.Estado.PREPARADA);
        List<PracticaRol> activas    = practicaRepo.findByAlumnoAndEstado(alumno, PracticaRol.Estado.ACTIVA);
        return !preparadas.isEmpty() || !activas.isEmpty();
    }

    public List<PracticaRol> getPracticasByAlumno(AlumnoRol alumno) {
        return practicaRepo.findByAlumno(alumno);
    }
}