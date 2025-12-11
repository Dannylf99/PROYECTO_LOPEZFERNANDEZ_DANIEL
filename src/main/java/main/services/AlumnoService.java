package main.services;

import org.springframework.stereotype.Service;

import main.repositories.AlumnoRepository;
import main.roles.AlumnoRol;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepo;

    public AlumnoService(AlumnoRepository alumnoRepo) {
        this.alumnoRepo = alumnoRepo;
    }

    public AlumnoRol login(String email, String contrasenya) {
        return alumnoRepo.findByEmailAndContrasenya(email, contrasenya).orElse(null);
    }

    public List<AlumnoRol> getAllAlumnos() {
        return alumnoRepo.findAll();
    }

    public AlumnoRol saveAlumno(AlumnoRol alumno) {
        return alumnoRepo.save(alumno);
    }
}
