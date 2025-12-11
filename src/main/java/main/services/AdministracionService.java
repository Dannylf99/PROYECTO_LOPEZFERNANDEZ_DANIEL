package main.services;

import org.springframework.stereotype.Service;

import main.repositories.AdministracionRepository;
import main.roles.AdministracionRol;

import java.util.List;

@Service
public class AdministracionService {

    private final AdministracionRepository adminRepo;

    public AdministracionService(AdministracionRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public AdministracionRol login(String email, String contrasenya) {
        return adminRepo.findByEmailAndContrasenya(email, contrasenya).orElse(null);
    }

    public List<AdministracionRol> getAllAdministradores() {
        return adminRepo.findAll();
    }
}
