package main.services;

import org.springframework.stereotype.Service;

import main.repositories.GestorRepository;
import main.roles.GestorRol;

import java.util.List;

@Service
public class GestorService {

    private final GestorRepository gestorRepo;

    public GestorService(GestorRepository gestorRepo) {
        this.gestorRepo = gestorRepo;
    }

    public GestorRol login(String email, String contrasenya) {
        return gestorRepo.findByEmailAndContrasenya(email, contrasenya).orElse(null);
    }

    public List<GestorRol> getAllGestores() {
        return gestorRepo.findAll();
    }
}
