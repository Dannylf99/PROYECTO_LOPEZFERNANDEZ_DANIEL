package main.services;

import org.springframework.stereotype.Service;
import main.repositories.CoordinadorRepository;
import main.roles.CoordinadorRol;

import java.util.List;

@Service
public class CoordinadorService {

    private final CoordinadorRepository coordinadorRepo;

    public CoordinadorService(CoordinadorRepository coordinadorRepo) {
        this.coordinadorRepo = coordinadorRepo;
    }

    public CoordinadorRol login(String email, String contrasenya) {
        return coordinadorRepo.findByEmailAndContrasenya(email, contrasenya).orElse(null);
    }

    public CoordinadorRol findByEmail(String email) {
        return coordinadorRepo.findByEmail(email).orElse(null);
    }

    public List<CoordinadorRol> getAllCoordinadores() {
        return coordinadorRepo.findAll();
    }
}