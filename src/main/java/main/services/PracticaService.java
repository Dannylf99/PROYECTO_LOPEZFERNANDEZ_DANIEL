package main.services;

import org.springframework.stereotype.Service;

import main.repositories.PracticaRepository;
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
}
